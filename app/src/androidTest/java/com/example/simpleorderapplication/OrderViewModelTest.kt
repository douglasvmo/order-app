package com.example.simpleorderapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.simpleorderapplication.data.repository.MockRepository
import com.example.simpleorderapplication.domain.Order
import com.example.simpleorderapplication.ui.order.OrderAction
import com.example.simpleorderapplication.ui.order.OrderViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrderViewModelTest {
    private lateinit var repository: MockRepository
    private lateinit var viewModel: OrderViewModel

    private val initialOrder = Order(
        id = "order-1",
        code = "1",
        clientName = "Joaquin Jose da Silva Xavier",
        clientCode = "123123123",
        clientPhone = "777777777",
        products = emptyList()
    )

    @Before
    fun setUp() {
        repository = MockRepository().apply { data.value = listOf(initialOrder) }
        viewModel = OrderViewModel(repository)
    }

    @Test
    fun OrderViewModel_Should_Initialize_With_Orders_From_Repository() = runBlocking {
        viewModel.state.test {
            var state = awaitItem()
            while (state.orders.none { it.id == initialOrder.id }) {
                state = awaitItem()
            }

            assertEquals(listOf(initialOrder), state.orders)
            assertNull(state.currentOrder)
            assertFalse(state.isLoading)
            assertNull(state.erro)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun OrderViewModel_Should_Create_An_Order() = runBlocking {
        viewModel.state.test {
            var state = awaitItem()
            while (state.orders.none { it.id == initialOrder.id }) {
                state = awaitItem()
            }

            val action = OrderAction.CreateOrder(
                clientName = "Antonio Vicente Mendes Maciel",
                clientPhone = "999999999",
                clientCPF = "44444444404"
            )
            viewModel.dispatch(action)

            while (state.orders.none{ it.clientName == action.clientName}){
                state = awaitItem()
            }

            val order = state.orders.single { it.clientName == action.clientName }

            while (state.currentOrder?.id != order.id) {
                state = awaitItem()
            }

            assertEquals(action.clientName, order.clientName)
            assertEquals(action.clientPhone, order.clientPhone)
            assertEquals(action.clientCPF, order.clientCode)
            assertEquals("2", order.code)
            assertEquals(order.id, state.currentOrder.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun OrderViewModel_Should_Add_Item_To_Selected_Order() = runBlocking {
        viewModel.state.test {
            var state = awaitItem()

            while (state.orders.none { it.id == initialOrder.id }) {
                state = awaitItem()
            }

            viewModel.dispatch(OrderAction.SelectOrder(initialOrder.id))
            while (state.currentOrder?.id != initialOrder.id) {
                state = awaitItem()
            }

            viewModel.dispatch(
                OrderAction.AddProduct(
                    description = "Café",
                    price = "12,50",
                    quantity = "2"
                )
            )

            while (state.currentOrder?.products?.any { it.description == "Café" } != true) {
                state = awaitItem()
            }

            val product = state.currentOrder.products.single()
            assertEquals("Café", product.description)
            assertEquals(1_250, product.priceCents)
            assertEquals(2.0, product.quantity, 0.0)
            assertTrue(product.id.isNotBlank())
            assertEquals(listOf(product), state.orders.single { it.id == initialOrder.id }.products)
            assertEquals(listOf(product), repository.data.value.single().products)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
