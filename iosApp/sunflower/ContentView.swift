//
//  ContentView.swift
//  sunflower
//
//  Created by Geunho on 2023/01/26.
//

import SwiftUI

struct ContentView: View {
    @ObservedObject var viewModel: PlantListViewModel = PlantListViewModel()
    
    var columns: [GridItem] = Array(repeating: .init(.flexible()), count: 2)
    
    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns) {
                ForEach(viewModel.plants, id: \.self) { plant in
                    Text(plant.name)
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
