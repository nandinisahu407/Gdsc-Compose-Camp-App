package com.example.new_year_resolutions

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.new_year_resolutions.ui.theme.Green1
import com.example.new_year_resolutions.ui.theme.Green2
import com.example.new_year_resolutions.ui.theme.New_year_resolutionsTheme
import com.example.new_year_resolutions.ui.theme.Yellow1
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    //state for ui
    private val resolutionsState = mutableStateOf(resolutions)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            New_year_resolutionsTheme {

                MyScreen(resolutionsState)

            }
        }
    }
}

@Composable
fun MyScreen(resolutionsState: MutableState<List<Resolution>>){
    val gradientColors= listOf(
        Color(0xFFCBF3F0),
        Color(0xFFFFFFFF),
        Color(0xFFFFDDAF)
    )

    //create brush with linear gradient
    val gradientBrush=Brush.linearGradient(
        colors = gradientColors
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ){

        MyWaveImageHeader(resolutionsState)
        Spacer(modifier = Modifier.padding(4.dp))

        LazyColumnEg(resolutionsState.value,
            onDelete = { resolution ->

                resolutionsState.value = resolutionsState.value.filterNot { it == resolution }

            },

            onEdit = { resolution ->
                resolutionsState.value = resolutionsState.value.toMutableList().apply {
                    val index = this.indexOf(resolution)
                    if (index != -1) {
                        this[index].name = resolution.editedName.value
                    }
                    println("\n\n----------- After Updating : " + resolutionsState + "-------------\n\n")
                }
            }
        )



    }



}

@Composable
fun MyWaveImageHeader(resolutionsState: MutableState<List<Resolution>>){

    var showBottomSheet=remember{
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.vector_5),
            contentDescription = "top header wave",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        //text on top of wave image
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            verticalArrangement = Arrangement.Center
        ){

            Text(
                text = "My New Year’s ",
                fontSize = 24.sp,
                fontFamily = FontFamily(
                    Font(R.font.playfairdisplay_regular)
                ),
                fontWeight = FontWeight(400),

                )

            Text(
                text = "Resolution",
                fontSize = 40.sp,
                fontFamily = FontFamily(
                    Font(R.font.playfairdisplay_black)
                ),

                fontWeight = FontWeight(500),
                color = Green1

            )

        }

        // Floating Action Button
        FloatingActionButton(
            onClick = {
                showBottomSheet.value=true
            },
            modifier = Modifier
                .padding(top = 26.dp, end = 30.dp)
                .align(Alignment.BottomEnd),

            backgroundColor = Yellow1,


            ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null,tint= Color.White)

        }


        if (showBottomSheet.value) {
            BottomSheetCompose(showBottomSheet) { newResolution ->
                resolutionsState.value = resolutionsState.value + Resolution(newResolution)
                showBottomSheet.value = false // Close the bottom sheet after adding the resolution
            }
        }




    }


}




//for custom list
@Composable
fun CustomItem(resolution: Resolution, onDelete: (Resolution) -> Unit,  onEdit: (Resolution) -> Unit){

    val context= LocalContext.current

    var showDeleteDialog = remember { mutableStateOf(false) }

    var showEditDialog = remember { mutableStateOf(false) }

    val gradientColors= listOf(
        Color(0xFF2EC4B6),
        Color(0xFFCBF3F0)
    )

    //create brush with linear gradient
    val custombrush=Brush.linearGradient(
        colors = gradientColors
    )


    Card(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(20.dp)

    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically ,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .background(brush = custombrush)

        ){
            Text(
                text=resolution.name,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                fontWeight = FontWeight(500),
                color = Green2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)

            )
            Spacer(modifier = Modifier.padding(5.dp))

            Image(painter = painterResource(id = R.drawable.baseline_edit_24),
                contentDescription = "edit icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 2.dp)
                    .clickable {
                        showEditDialog.value = true
                    }

            )

            Image(painter = painterResource(id = R.drawable.delete_icon),
                contentDescription ="delete icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .padding(end = 2.dp)
                    .clickable {
                        showDeleteDialog.value = true
                    }

            )



            // Show alert dialog when showDialog is true
            if (showDeleteDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog.value = false
                    },
                    title = {
                        Text(
                            text = "Confirm Deletion",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Text("Are you sure you want to delete this resolution?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onDelete.invoke(resolution)
                                showDeleteDialog.value = false
                                Toast.makeText(context,"Successfully Deleted !!",Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Yes", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDeleteDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("No", color = Color.Black)
                        }
                    }
                )
            }

            // Show edit dialog when showEditDialog is true
            if (showEditDialog.value) {

                // Initialize with the current name
                var editedName by remember { mutableStateOf(resolution.name) }


                AlertDialog(
                    onDismissRequest = {
                        showEditDialog.value = false
                    },
                    title = {
                        Text(
                            text = "Edit Resolution",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        OutlinedTextField(
                            value = resolution.editedName.value,
                            onValueChange = { resolution.editedName.value = it },
                            label = { Text(text = "Edit Resolution") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 18.sp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                onEdit.invoke(resolution.copy(name = editedName))
                                showEditDialog.value = false
                                Toast.makeText(context, "Successfully Edited !!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showEditDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Cancel", color = Color.Black)
                        }
                    }
                )
            }


        }

    }


}


@Composable
fun LazyColumnEg(categories: List<Resolution> , onDelete: (Resolution) -> Unit , onEdit: (Resolution) -> Unit){
    LazyColumn(modifier = Modifier.padding(vertical = 8.dp)){

        categories.forEach{ resolution ->

            item {
                CustomItem(
                    resolution = resolution,
                    onDelete = { onDelete.invoke(resolution) },
                    onEdit = { onEdit.invoke(resolution) }
                )
            }
        }
    }

}



@Composable
fun BottomSheetCompose(
    showBottomSheet: MutableState<Boolean>,
    onAddResolution: (String) -> Unit
): MutableState<Boolean> {

    val context= LocalContext.current

    // State to hold the text entered by the user
    var resolutionText by remember { mutableStateOf("") }

    if (showBottomSheet.value) {
        AlertDialog(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            title = {
                Text(
                    text = "Add New Resolution",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                OutlinedTextField(
                    value = resolutionText,
                    onValueChange = { resolutionText = it },
                    label = { Text(text = "Enter Resolution") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 18.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBottomSheet.value = false
                        Toast.makeText(context,"Successfully Added Resolution ✅",Toast.LENGTH_SHORT).show()
                        onAddResolution(resolutionText)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                    shape = RoundedCornerShape(50)

                ) {
                    Text("Add", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showBottomSheet.value = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    shape = RoundedCornerShape(50)


                ) {
                    Text("Cancel", color = Color.Black)
                }
            }
        )
    }

    return showBottomSheet
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    New_year_resolutionsTheme {
//        MyScreen()

    }
}