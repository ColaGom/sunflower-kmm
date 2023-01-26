//
//  sunflowerApp.swift
//  sunflower
//
//  Created by Geunho on 2023/01/26.
//

import SwiftUI
import shared

@main
struct sunflowerApp: App {
    
    init() {
        KoinIosKt.startKoin()
        print("start KOIN")
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
