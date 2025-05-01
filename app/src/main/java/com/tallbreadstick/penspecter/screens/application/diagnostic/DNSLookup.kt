package com.tallbreadstick.penspecter.screens.application.diagnostic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tallbreadstick.penspecter.ui.theme.DarkGray
import com.tallbreadstick.penspecter.ui.theme.PaleBlue
import com.tallbreadstick.penspecter.ui.theme.Roboto
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tallbreadstick.penspecter.menus.Navbar
import com.tallbreadstick.penspecter.models.DomainInfoResponse
import com.tallbreadstick.penspecter.ui.theme.VeryDarkGray
import com.tallbreadstick.penspecter.viewmodels.DNSViewModel
import kotlinx.coroutines.launch

@Preview
@Composable
fun DNSLookup(navController: NavController? = null, viewModel: DNSViewModel? = null) {

    val sidebarOpen = remember { mutableStateOf(false) }
    val queryTypes = listOf("RESOLVE", "REVERSE", "INFO")
    val queryType = remember { mutableStateOf("RESOLVE") }
    val domain = remember { mutableStateOf("") }
    val domainInfo = remember { mutableStateOf<DomainInfoResponse?>(null) }
    val resultList = remember { mutableStateListOf<Pair<String, String?>>() }
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        Navbar(navController, sidebarOpen)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .background(VeryDarkGray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                if (resultList.isNotEmpty()) {
                    StyledDnsResult(resultList)
                } else if (domainInfo.value != null) {
                    StyledDomainInfo(domainInfo.value!!)
                } else {
                    Text(if (isLoading.value) "Searching..." else "No Results", color = Color.White, fontSize = 16.sp)
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGray)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = domain.value,
                        onValueChange = { domain.value = it },
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontFamily = Roboto),
                        placeholder = {
                            Text("Enter domain or address", fontSize = 16.sp, fontFamily = Roboto, color = Color.LightGray)
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.DarkGray,
                            focusedContainerColor = Color.DarkGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.DarkGray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                            .weight(1.5f)
                            .clickable { expanded = true }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = queryType.value,
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = Roboto,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            queryTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        queryType.value = type
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = PaleBlue, contentColor = DarkGray),
                        shape = RectangleShape,
                        onClick = {
                            resultList.clear()
                            isLoading.value = true
                            when (queryType.value) {
                                "RESOLVE" -> {
                                    viewModel?.fetchResolvedDns(domain.value) { result ->
                                        resultList.addAll(result)
                                        isLoading.value = false;
                                    }
                                }
                                "REVERSE" -> {
                                    viewModel?.fetchReversedDns(domain.value) { result ->
                                        resultList.addAll(result)
                                        isLoading.value = false;
                                    }
                                }
                                "INFO" -> {
                                    viewModel?.fetchDomainInfo(domain.value) { result ->
                                        result?.let {
                                            domainInfo.value = it // Store the result in a state variable
                                        } ?: run {
                                            resultList.add("Error" to "Failed to fetch domain info")
                                        }
                                        isLoading.value = false
                                    }
                                }
                                else -> {
                                    resultList.add("Error" to "Unknown query type!")
                                    isLoading.value = false;
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Lookup",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StyledDnsResult(results: List<Pair<String, String?>>) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Box {
        Column {
            results.forEach { (key, value) ->
                val isError = key == "Error" || value == null || value == "null"

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "$key: ",
                        color = Color(0xFFFFA500), // Orange
                        fontSize = 16.sp
                    )
                    value?.let {
                        Text(
                            text = it,
                            color = if (isError) Color.Red else Color(0xFF00FF00), // Red or Green
                            fontSize = 16.sp,
                            modifier = Modifier.clickable {
                                clipboardManager.setText(AnnotatedString(it))
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Copied: $it")
                                }
                            }
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun StyledDomainInfo(domainInfo: DomainInfoResponse) {
    Column {
        Text("Domain: ${domainInfo.domain}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

        if (!domainInfo.tags.isNullOrEmpty()) {
            Text("Tags: ${domainInfo.tags.joinToString(", ")}", fontSize = 16.sp, color = Color.Cyan)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Subdomains:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Magenta)
        domainInfo.subdomains?.forEach { subdomain ->
            Text("- $subdomain", fontSize = 14.sp, color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("DNS Records:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Green)
        domainInfo.data.forEach { entry ->
            Text(
                "${entry.type} - ${entry.subdomain}: ${entry.value}",
                fontSize = 14.sp,
                color = Color.Yellow
            )
        }
    }
}
