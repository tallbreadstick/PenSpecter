package com.tallbreadstick.penspecter.models

data class DomainInfoResponse(
    val domain: String,
    val tags: List<String>?,
    val data: List<DnsEntry>,
    val subdomains: List<String>?,
    val more: Boolean
)

data class DnsEntry(
    val subdomain: String,
    val type: String,
    val value: String,
    val last_seen: String
)
