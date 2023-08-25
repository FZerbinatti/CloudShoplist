package com.example.cloudshoplist

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.xml.validation.Validator

@RunWith(JUnit4::class)
class InputValidatorTest{

    @Test
    fun whenInputIsValid(){
        val item = "caramelle"
        val result = InputValidator.validateInput(item)
        assertThat(result).isEqualTo(true)

    }

    @Test
    fun whenInputIsInvalid(){
        val item = "caramelle!"
        val result = InputValidator.validateInput(item)
        assertThat(result).isEqualTo(true)

    }

}