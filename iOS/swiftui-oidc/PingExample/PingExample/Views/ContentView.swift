import SwiftUI

struct ContentView: View {
    
    @State private var startDavinici = false
    
    @State private var path: [String] = []
    
    @State private var configurationViewModel: ConfigurationViewModel = ConfigurationManager.shared.loadConfigurationViewModel()
    
    @ObservedObject var oidcViewModel: OIDCViewModel = OIDCViewModel()
    
    @ObservedObject var logoutViewModel: LogoutViewModel = LogoutViewModel()
    
    var body: some View {
        NavigationStack(path: $path) {
            List {
                Section(header: Text("Configuration")) {
                    NavigationLink(value: "Configuration") {
                        Text("Edit configuration")
                    }
                }
                Section(header: Text("Storage Items")) {
                    NavigationLink(value: "Token") {
                        Text("Access Token")
                    }
                    NavigationLink(value: "User") {
                        Text("User Info")
                    }
                }
                Section(header: Text("Actions")) {
                    Button(action: {
                        Task {
                            do {
                                let _ = try await oidcViewModel.startOIDC()
                                path.append("Token")
                            } catch {
                                print(String(describing: error))
                            }
                        }
                    }) {
                        Text("Launch OIDC")
                    }
                    Button(action: {
                        Task {
                            await logoutViewModel.logout()
                        }
                    }) {
                        Text("Logout")
                    }
                }
            }
            .navigationDestination(for: String.self) { item in
                switch item {
                case "Configuration":
                    ConfigurationView(viewmodel: $configurationViewModel)
                case "Token":
                    AccessTokenView(path: $path)
                case "User":
                    UserInfoView(path: $path)
                default:
                    EmptyView()
                }
            }
            .navigationBarTitle("Ping OIDC")
            Text($oidcViewModel.status.wrappedValue)
            Image(uiImage: UIImage(named: "Logo")!)
                .resizable()
                .frame(width: 180.0, height: 180.0).clipped()
            
            
        }
        .onAppear{
            Task {
                do {
                    let _ = try await ConfigurationManager.shared.startSDK()
                    self.oidcViewModel.updateStatus()
                } catch {
                    self.oidcViewModel.status = String(describing: error)
                    print(String(describing: error))
                }
            }
        }
    }
}

struct NextButton: View {
    let title: String
    let action: () -> (Void)
    var body: some View {
        Button(action:  {
            action()
        } ) {
            Text(title)
                .font(.headline)
                .foregroundColor(.white)
                .padding()
                .frame(width: 300, height: 50)
                .background(Color.green)
                .cornerRadius(15.0)
                .shadow(radius: 10.0, x: 20, y: 10)
        }
    }
}

@main
struct MyApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
