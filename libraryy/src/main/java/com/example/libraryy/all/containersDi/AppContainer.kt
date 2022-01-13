package com.example.libraryy.all.containersDi

interface AppContainer {
    fun plusPersonDetailsComponent(): ContactDetailsContainer

    fun plusPersonListComponent(): ContactListContainer
}