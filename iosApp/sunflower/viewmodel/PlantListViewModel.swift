//
//  PlantListViewModel.swift
//  sunflower
//
//  Created by Geunho on 2023/01/26.
//

import Foundation
import shared

class PlantListViewModel : ObservableObject {
    private let store = StoreComponent().plantList(growZone: -1)
    @Published var plants: [Plant] = []
    
    init() {
        CFlowKt.watch(store.state) { (state) in
            if let state = state as? PlantListState {
                self.plants = state.plants
            }
        }
    }
}
