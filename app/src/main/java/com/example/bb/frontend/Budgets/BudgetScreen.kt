package com.example.bb.frontend.Budgets

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bb.backend.Budget
import com.example.bb.backend.Category
import com.example.bb.backend.Expense
import com.example.bb.backend.User
import com.example.bb.frontend.*
import com.example.bb.frontend.Calendar.curExpenseEdit

@Composable
fun BudgetScreen(u: User) {
    budNav = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(
            backgroundColor = Color.White
        ){ budgetSelection(u) }},
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                budNavigation(budNav, u)
            }
        },
        backgroundColor = Color(50,100,50)
    )

}

//navigation
lateinit var cat: Category
lateinit var bud: Budget
lateinit var budNav: NavHostController
@Composable
fun budNavigation(navController: NavHostController, u: User) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            mainBudScreen(u)
        }
        composable("categoryInfo") {
            categoryDetail(u, cat)
        }
        composable("newBud") {
            EditBudget(u = u, b = bud)
        }
    }
}

//the content in the center
@Composable
fun mainBudScreen(u: User) {


    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        //overall budget
        if(u.activeBud!=-1) {// if available budget
            var b = u.budgets[u.activeBud]
            budget(b)

            //categories
            for (c in b.categories) {
                category(c)
            }
        } else {    //if no budget selected
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 15.dp)
                    .height(120.dp),
                elevation = 10.dp
            ) {
                Text(
                    "No Budget Selected",
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(0.dp,30.dp)
                )
            }
        }
    }
}

//overall budget info
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun budget(budget: Budget) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 15.dp)
            .height(120.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            //text
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {

                Text(text= budget.name)
                Text(text= String.format("$%.2f / $%.2f", budget.total(), budget.income))
            }
            //progrss bar
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
                progress = budget.percent().toFloat()/100)

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {

                //left side of bottom row
                Text(" ")
                //right side
                Button( onClick = {
                    bud = budget
                    budNav.navigate("newBud")
                },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(40.dp)
                        .width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0,140,0),
                        contentColor = Color.White
                    )
                ){
                    Text("Edit")
                }
            }
        }
    }
}

//displays a category in the content section
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun category(category: Category) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp, 15.dp)
            .clickable { },
        elevation = 10.dp,
        onClick = {
            cat = category
            budNav.navigate("categoryInfo")
        }
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            //text
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {

                Text(text= category.name)
                Text(text= String.format("$%.2f / $%.2f", category.total(), category.cap))
            }
            //progrss bar
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
                                    progress = category.percent().toFloat()/100)
        }
    }
}

//the dropdown at the top of the screen
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun budgetSelection(u: User) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var options = ArrayList<Budget>()
        for (b in u.budgets)
            options.add(b)
        options.add(Budget("--New Budget--", -1.0))

        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0].name) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 0.dp)
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            if (selectionOption.name.equals("--New Budget--")) {
                                bud = selectionOption
                                budNav.navigate("newBud")
                                u.budgets.add(selectionOption)
                            } else {
                                //update page... hella sketchy tho
                                u.activeBud = u.budgets.indexOf(selectionOption)
                                selectedOptionText = selectionOption.name
                                expanded = false
                                budNav.navigate("main")
                            }
                            u.activeBud = u.budgets.indexOf(selectionOption)
                            selectedOptionText = selectionOption.name
                            expanded = false


                        }
                    ) {
                        Text(text = selectionOption.name)
                    }
                }
            }
        }
    }
}

//bar at the bottom
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun addExpense(c: Category, u: User) {

    //expense data
    var expName by remember { mutableStateOf(TextFieldValue()) }
    var expCost by remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(90.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        //name
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {

                Text(
                    "Name",
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .height(15.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                TextField(
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .width(130.dp),
                    value = expName,
                    onValueChange = { newText ->
                        expName = newText
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White
                    )
                )
            }
        //cost
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {

                Text(
                    "Cost",
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .height(15.dp),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                TextField(
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 0.dp)
                        .width(80.dp),
                    value = expCost,
                    onValueChange = { newText ->
                        expCost = newText
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White
                    )
                )
            }
        val openDialogCat = remember { mutableStateOf(false) }
        val openDialogBud = remember { mutableStateOf(false) }
        //enter
        Button(
            onClick = {
                try {
                    //System.out.println("2: "+category.name)
                    var e = Expense(expName.text, expCost.text.toDouble())
                    c.addExpense(e)
                } catch (e: java.lang.Exception) {
                }
                //if alerts needed
                if(c.cap!=0.0 && 100-c.percent() <= u.alertSetting.categoryPercent) {
                    openDialogCat.value = true
                }
                if(100-c.bud.percent() <= u.alertSetting.budgetPercent) {
                    openDialogBud.value = true
                }
                if(!(openDialogCat.value || openDialogBud.value)) {

                    //go to current page (update it)
                    var route = budNav.currentBackStackEntry?.destination?.route
                    //System.out.println("route: \""+route.toString()+"\"")
                    budNav.navigate(route.toString())
                }
            },
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterVertically)
                .width(80.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0, 140, 0),
                contentColor = Color.White
            )
        ) {Text("Enter")}

        //if went over budget
        if (openDialogBud.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialogBud.value = false
                },
                title = {
                    Text(text = "Close to going over budget")
                },
                text = {
                    Text(
                        String.format("There is less than %.0f%% remaining in the entire budget", (100-c.bud.percent()))
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                openDialogBud.value = false

                                //go to current page (update it)
                                var route = budNav.currentBackStackEntry?.destination?.route
                                //System.out.println("route: \""+route.toString()+"\"")
                                budNav.navigate(route.toString())}
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            )
        }
        if (openDialogCat.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialogCat.value = false
                },
                title = {
                    Text(text = "Close to going over budget")
                },
                text = {
                    Text(
                        String.format("There is less than %.0f%% remaining in the Category", (100-c.percent()))
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                openDialogCat.value = false

                                //go to current page (update it)
                                var route = budNav.currentBackStackEntry?.destination?.route
                                //System.out.println("route: \""+route.toString()+"\"")
                                budNav.navigate(route.toString())}
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            )
        }
    }
}

//new screen with details of the category
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun categoryDetail(u:User, category: Category) {
    Scaffold(
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                //category info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp, 15.dp)
                        .clickable { },
                    elevation = 10.dp,
                    onClick = {
                        cat = category
                        budNav.navigate("main")
                    }
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        //text
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {

                            Text(text= category.name)
                            Text(text= String.format("$%.2f / $%.2f", category.total(), category.cap))
                        }
                        //progrss bar
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),
                            progress = category.percent().toFloat()/100)
                    }
                }

                //displaying expenses
                Text("Expenses", modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    color = Color.White)
                for(e in category.expenses) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            //text = "${e.date}  ${mDate.value}",
                            text = "${e.category.name} Day: ${e.date.toString().substring(8,10)}\n${e.name} $${String.format("%.2f  ", e.cost)}",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                            color = androidx.compose.ui.graphics.Color.White
                        )

                        Button(onClick = { curExpenseEdit = e
                            navController.navigate("editExpense")},
                            colors = ButtonDefaults.buttonColors(backgroundColor = androidx.compose.ui.graphics.Color.White)){
                            Text(text = "Edit", color = androidx.compose.ui.graphics.Color.Black)
                        }
                    }
                }
            }
        },
        bottomBar = { addExpense(category,u)},//change
        backgroundColor = Color(50,100,50)
    )

}