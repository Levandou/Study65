package com.velagissellint.a65.all.containersDi

interface AppContainer {
    fun plusPersonDetailsComponent(): ContactDetailsContainer

    fun plusPersonListComponent(): ContactListContainer
}
