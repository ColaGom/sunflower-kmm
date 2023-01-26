//
//  ContentView.swift
//  sunflower
//
//  Created by Geunho on 2023/01/26.
//

import SwiftUI

struct ContentView: View {
    @ObservedObject var viewModel: PlantListViewModel = PlantListViewModel()
    
    
    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]
    
    var body: some View {
        ScrollView {
            LazyVGrid(columns: columns) {
                ForEach(viewModel.plants, id: \.self) { plant in
                    VStack {
                        Text(plant.name)
                        AsyncImage(url: URL(string: plant.imageUrl)) { phase in
                            phase.image?.resizable()
                                .aspectRatio(contentMode: .fit)
                        }
                        
                    }.frame(height:150)
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

extension UIImageView {
    func loadFrom(URLAddress: String) {
        guard let url = URL(string: URLAddress) else {
            return
        }
        
        DispatchQueue.main.async { [weak self] in
            if let imageData = try? Data(contentsOf: url) {
                if let loadedImage = UIImage(data: imageData) {
                    self?.image = loadedImage
                }
            }
        }
    }
}
