package com.example.bb.frontend.Budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bb.backend.Budget
import com.example.bb.backend.Category
import com.example.bb.backend.User

@Composable
fun EditBudget(u: User, b: Budget) {
    var categories by remember { mutableStateOf(b.categories) }

    var budgetName by remember { mutableStateOf(b.name) }
    var budgetIncome by remember { mutableStateOf(b.income.toString()) }
    var limits = ArrayList<String>()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        // save and delete
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            //save
            Button(
                onClick = {
                    try {
                        b.name = budgetName
                        b.income = budgetIncome.toDouble()
                        //save caps
                        var i = 0
                        for(s in limits) {
                            try {
                                categories.get(i).cap = s.toDouble()
                            }catch (e: java.lang.Exception){}
                            i++
                        }
                    } catch(e: java.lang.Exception) {}
                    budNav.navigate("main")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .height(35.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Save & Exit")//works
            }
            //delete
            Button(
                onClick = {

                    u.budgets.remove(b)
                    if(u.budgets.size == 0)
                        u.activeBud = -1
                    else
                        u.activeBud = 0
                    budNav.navigate("main")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .height(35.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text("Delete")
            }
        }

        //******************************************************************************************
        //name
        Text(
            "Budget",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 30.sp
        )
        Text(
            "Budget Name",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .height(20.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        TextField(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
            value = budgetName,
            onValueChange = { newText ->
                budgetName = newText
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White
            )
        )
        //************************************
        //income
        Text(
            "Budget Income",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .height(20.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        TextField(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
            value = budgetIncome,
            onValueChange = { newText ->
                budgetIncome = newText
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White
            )
        )
        //******************************************************************************************

        Spacer(modifier = Modifier.size(50.dp))
        Text(
            "Categories",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 30.sp
        )

        for(c in categories) {
            Row(){
                var name by remember { mutableStateOf(c.name) }
                var limit by remember { mutableStateOf(c.cap.toString()) }
                limits.add(limit)
                //category name
                Column() {
                    Text(
                        "Category Name",
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 0.dp)
                            .height(20.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    TextField(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 0.dp)
                            .width(130.dp),
                        value = name,
                        onValueChange = { newText ->
                            name = newText
                            c.name = name // its immediately saved
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White
                        )
                    )
                }
                //category cap
                Column() {
                    Text(
                        "Category Limit",
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 0.dp)
                            .height(20.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    TextField(
                        modifier = Modifier
                            .padding(horizontal = 0.dp, vertical = 0.dp)
                            .width(130.dp),
                        value = limit,
                        onValueChange = { newText ->
                            limit = newText
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.Black,
                            backgroundColor = Color.White
                        )
                    )
                }
                //remove
                Button(
                    onClick = {


                        b.name = budgetName
                        b.income = budgetIncome.toDouble()
                        //save caps
                        var i = 0
                        for(s in limits) {
                            try {
                                categories.get(i).cap = s.toDouble()
                            }catch (e: java.lang.Exception){}
                            i++
                        }

                        categories.remove(c)

                        budNav.navigate("newBud")
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(35.dp)
                        .align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Remove")
                }
            }
        }
        Button(
            onClick = {
                categories.add(Category("New Category", 0.0))

                //saving
                b.name = budgetName
                b.income = budgetIncome.toDouble()
                //save caps
                var i = 0
                for(s in limits) {
                    try {
                        categories.get(i).cap = s.toDouble()
                    }catch (e: java.lang.Exception){}
                    i++
                }
                //update page
                budNav.navigate("newBud")
            },
            modifier = Modifier
                .padding(16.dp)
                .height(35.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("New Category")
        }

    }
}