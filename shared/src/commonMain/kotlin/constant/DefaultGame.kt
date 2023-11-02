package constant

enum class DefaultGame(val showName: String, val loadName: String) {
    Bomber(showName = "bomber", loadName = "bomber.txt"),
    Sidecar(showName = "sidecar", loadName = "sidecar.txt"),
    Siesta(showName = "siesta", loadName = "siesta.txt"),
    SkewedTrafficLight(showName = "skewed_traffic_light", loadName = "skewed_traffic_light.txt"),
    TNosed(showName = "t_nosed", loadName = "t_nosed.txt"),
}