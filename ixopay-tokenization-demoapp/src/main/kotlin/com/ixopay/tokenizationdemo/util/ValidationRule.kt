package com.ixopay.tokenizationdemo.util

//A predicate is a function that evaluates to true when its param matches the condition of the predicate
typealias ValidatorPredicate = (value: String?) -> Boolean

data class ValidationRule(val predicate: ValidatorPredicate, val errorMessage: String)