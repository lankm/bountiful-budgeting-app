package com.example.bb.frontend

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bb.backend.User
import com.example.bb.frontend.Budgets.BudgetScreen
import com.example.bb.frontend.Calendar.editExpense
import com.example.bb.frontend.Reports.displayReport
import com.example.bb.frontend.Reports.displayedReport
import com.example.bb.frontend.Settings.AlertScreen
import com.example.bb.frontend.Settings.SettingScreen

sealed class NavigationItem(var route: String, var icon : ImageVector, var title: String) {
    object Budget : NavigationItem("budget", Icons.Rounded.LineStyle, "Budget")
    object Calendar : NavigationItem("calendar", Icons.Rounded.CalendarMonth , "Expenses")
    object Report : NavigationItem("reports", Icons.Rounded.AddChart, "Reports")
    object Settings : NavigationItem("setting", Icons.Rounded.Settings, "Settings")

    object AlertSettings : NavigationItem("alert", Icons.Rounded.Settings, "Alert")
    object ReportDisplay : NavigationItem("displayReport", Icons.Rounded.Settings, "Report")
    object EditExpense : NavigationItem("EditExpense", Icons.Rounded.Settings, "ExpenseEditor")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Budget,
        NavigationItem.Calendar,
        NavigationItem.Report,
        NavigationItem.Settings
    )
    BottomNavigation(
        backgroundColor = Color(0,140,0),
        contentColor = Color.White,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title)},
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(1f),
                alwaysShowLabel = true,
                selected = false,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

lateinit var navController: NavHostController
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(u: User) {
    navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(
            backgroundColor = Color(0,140,0),
            contentColor = Color.White) {
            Text(
                text = "Bountiful Budgeting",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )}},
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                Navigation(navController, u)
            }
        },
        backgroundColor = Color(50,100,50) // Set background color to avoid the white flashing when you switch between screens
    )




}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController, u: User) {
    NavHost(navController, startDestination = NavigationItem.Budget.route) {
        composable(NavigationItem.Budget.route) {
            BudgetScreen(u)
        }
        composable(NavigationItem.Calendar.route) {
            CalendarScreen(u)
        }
        composable(NavigationItem.Report.route) {
            ReportScreen(u)
        }
        composable(NavigationItem.Settings.route) {
            SettingScreen(u)
        }

        composable(NavigationItem.AlertSettings.route) {
            AlertScreen(u)
        }
        composable(NavigationItem.ReportDisplay.route) {
            displayReport(u)
        }
        composable(NavigationItem.EditExpense.route) {
            editExpense(u)
        }
    }
}