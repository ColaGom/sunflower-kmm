// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "Shared",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "Shared",
            targets: ["Shared"])
    ],
    dependencies: [],
    targets: [
        .binaryTarget(
            name: "Shared",
            url: "https://maven.pkg.github.com/ColaGom/sunflower-kmm/com/samples/apps/sunflower/shared/1.0.2/shared-1.0.2.zip",
            checksum: "46e6210d5a275334a047c766ed21294cd1594b65343fdb343ffa7ad4130ce781"
        )
    ]
)