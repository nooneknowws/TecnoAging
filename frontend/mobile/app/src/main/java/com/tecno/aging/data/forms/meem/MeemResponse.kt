package com.tecno.aging.data.forms.meem

import com.tecno.aging.domain.models.forms.meem.MeemPergunta

data class MeemResponse(
    val pergunta: MeemPergunta,
    var selectedRadio: Int? = null,
    val selectedCheckboxes: MutableList<Boolean> = mutableListOf(),
    var selectedRange: Int? = null
)
