package com.example.simpleorderapplication.ui

enum class AppScreen(val route: String) {
    CreateOrder("create/order/{orderId}"),
    OrderList("list/order"),
    OrderDetails("list/order/{orderId}"),
    AddProduct("create/order/{orderId}/product");

    fun withArgs(vararg args: Any): String {
        var finalRoute = route
        args.forEach { arg ->
            finalRoute = finalRoute.replaceFirst(Regex("\\{[^/]+\\}"), arg.toString())
        }
        return finalRoute
    }
}