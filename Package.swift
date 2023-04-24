// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: Shared,
    platforms: [
        .macOS(.v10_14), .iOS(.v13), .tvOS(.v13)
    ],
    products: [
        // Products define the executables and libraries a package produces, and make them visible to other packages.
        .library(
            name: Shared,
            targets: [Shared])
    ],
    dependencies: [
        // Dependencies declare other packages that this package depends on.
    ],
    targets: [
        .binaryTarget(
            name: Shared,
            url: https://maven.pkg.github.com/ColaGom/sunflower-kmm/com/samples/apps/sunflower/shared/1.0.4/shared.zip,
            checksum: 178ecc30d5ecb8bc89edb3dfbaf6fcae845d5fb5ddf802b96b96a5a819956e56
        )
    ]
)