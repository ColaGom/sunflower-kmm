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
            url: "https://maven.pkg.github.com/ColaGom/sunflower-kmm/com/samples/apps/sunflower/shared/1.0.0/shared-1.0.0.zip",
            checksum: "f4df5f9f3a6bf35754fc157fad012e5cdbff8bf47b2d92b8e35840406344ffb6"
        )
    ]
)