# Articles
- [Sunflower KMM: Get started with Kotlin multiplatform mobile](https://medium.com/@bchoi000/sunflower-flavor-kmm-get-started-with-kotlin-multiplatform-mobile-9dc014c45b95)
- [Sunflower KMM: Rescue iOS Developers from Khaos](https://medium.com/@bchoi000/sunflower-kmm-rescue-ios-developers-from-khaos-9e5fdb603ff6)

# iOS
https://github.com/ColaGom/sunflower-ios

# Android Sunflower with KMM
⚠️ ⚠️ This project is only a sample project to show the KMM project migration tasks. thus it has been omitted tasks for production project such as test code, optimization and logging.⚠️ ⚠️ 

## Result
Android            |  iOS
:-------------------------:|:-------------------------:
<img src = "screenshots/aos.png" width="50%" height="50%">  |  <img src = "screenshots/ios.png" width="50%" height="50%">  
### Migration Targets
- Data Layer
- Domain Layer
- Presentation Layer
  - Only `ViewModel`

### Migration Steps

1. Migrate all android specific dependencies of Targets
   - dagger-hilt => koin
   - room => sql-delight
   - retrofit => ktor

2. Extract `shared` module from `android`
3. Create ios(swift) project and link `shared` module
3. Implement UI using `shared` module
   - `PlantListView` using `PlantListStore`
