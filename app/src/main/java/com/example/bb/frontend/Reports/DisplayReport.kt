package com.example.bb.frontend.Reports

import android.util.Log
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
import com.example.bb.backend.Report
import com.example.bb.backend.User
import com.example.bb.frontend.Calendar.curExpenseEdit
import com.example.bb.frontend.navController
import java.lang.Exception

lateinit var displayedReport: Report
@Composable
fun displayReport(u: User) {

    //if not initiallized. is a dumb fix
    if (!::displayedReport.isInitialized) {
        displayedReport = u.budgets[0].reports[0]
    }

    //System.out.println("3:"+displayedReport.name)
    Column() {
        Button(
            onClick = {
                navController.navigate("reports")
            },
            modifier = Modifier
                .padding(16.dp)
                .height(35.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Exit")
        }

        Text(
            text = displayedReport.print(),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 0.dp),
            color = Color.White
        )
    }

}
