package edu.csuglobal.csc475.connectivitychecker

enum class ConnectivityLabel(val label: String, val minSpeed: Double, val maxSpeed: Double) {
    NONE("None", 0.0, 0.0),
    POOR("Poor", 0.1, 1.0),
    SLOW("Slow", 1.0, 3.0),
    OK("OK", 3.0, 5.0),
    GOOD("Good", 5.0, 20.0),
    EXCELLENT("Excellent", 20.0, Double.MAX_VALUE)
}