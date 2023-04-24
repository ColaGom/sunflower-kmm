// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "Shared",
    platforms: [
        .macOS(.v10_14), .iOS(.v13), .tvOS(.v13)
    ],
    products: [
        // Products define the executables and libraries a package produces, and make them visible to other packages.
        .library(
            name: "Shared",
            targets: ["Shared"])
    ],
    dependencies: [
        // Dependencies declare other packages that this package depends on.
    ],
    targets: [
        .binaryTarget(
            name: "Shared",
            url: "https://maven.pkg.github.com/ColaGom/sunflower-kmm/com/samples/apps/sunflower/shared/1.0.0/shared-1.0.0.zip",
            checksum: "3e4279c413072f5f65017ef7013708f42883793b7c06f36a1027c2f9da71663f"
        )
    ]
)
