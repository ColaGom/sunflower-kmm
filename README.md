# Android Sunflower with KMM

### Migration Targets
- Data Layer
- Domain Layer
- Presenter Layer
  - Only `ViewModel`

### Migration Steps

1. Migrate all android specific dependencies of Targets
   - dagger-hilt => koin
   - room => sql-delight
   - retrofit => ktor

2. Extract `shared` module from `android`
3. Implement `iosApp` using `shared`
4. Migrate `test` codes
