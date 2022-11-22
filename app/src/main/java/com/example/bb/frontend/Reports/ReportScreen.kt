
package com.example.bb.frontend

import android.graphics.Color
import android.util.Log
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius.Companion.Zero
import androidx.compose.ui.geometry.Size.Companion.Zero
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.bb.backend.*
import androidx.compose.ui.window.PopupProperties
import com.example.bb.MainActivity
import com.example.bb.R
import com.example.bb.frontend.Reports.displayReport
import com.example.bb.frontend.Reports.displayedReport


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReportScreen(u: User) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())) {
        //***************************************************
        Text("", modifier = Modifier.height(20.dp))
        Text(
            "Select a Budget",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .height(20.dp),
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )


        var selBudget = u.budgets[0]
        var selReport = selBudget.reports[0]

        //first dropbox
        val options = u.budgets
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0].name) }
        //second dropbox
        var options2 by remember { mutableStateOf(selBudget.reports) }
        var expanded2 by remember { mutableStateOf(false) }
        var selectedOptionText2 by remember { mutableStateOf(selReport.name)}

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = TextFieldDefaults.textFieldColors(textColor = androidx.compose.ui.graphics.Color.Black, backgroundColor = androidx.compose.ui.graphics.Color.White)
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
                            selBudget = selectionOption

                            //updating reports list
                            options2 = selBudget.reports
                            try {
                                selectedOptionText2 = options2[0].name
                            } catch(e: java.lang.Exception) {
                                selectedOptionText2 = "No Reports Available"
                            }

                            selectedOptionText = selectionOption.name
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption.name)
                    }
                }
            }
        }
        Text("", modifier = Modifier.height(20.dp))
        Text(
            "Select a Report",
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp)
                .height(20.dp),
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )
        //***********************************************************



        ExposedDropdownMenuBox(
            expanded = expanded2,
            onExpandedChange = {
                expanded2 = !expanded2
            }
        ) {
            TextField(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                readOnly = true,
                value = selectedOptionText2,
                onValueChange = { },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded2
                    )
                },
                colors = TextFieldDefaults.textFieldColors(textColor = androidx.compose.ui.graphics.Color.Black, backgroundColor = androidx.compose.ui.graphics.Color.White)
            )
            ExposedDropdownMenu(
                expanded = expanded2,
                onDismissRequest = {
                    expanded2 = false
                }
            ) {
                options2.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            displayedReport = selectionOption
                            selReport = selectionOption
                            //System.out.println("1:"+displayedReport.name)

                            selectedOptionText2 = selectionOption.name
                            expanded2 = false
                        }
                    ) {
                        Text(text = selectionOption.name)
                    }
                }
            }
        }
        //**********************************************
        Text("", modifier = Modifier.height(10.dp))
        //********
        Button(
            onClick = {
                //System.out.println("2:"+displayedReport.name)
                navController.navigate("displayReport")
            },
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
                .width(200.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = androidx.compose.ui.graphics.Color.White,
                contentColor = androidx.compose.ui.graphics.Color.Black
            )
        ) {
            Text("Display Report")
        }
    }
}