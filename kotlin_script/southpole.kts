import java.time.*

val instant = Instant.now()
val southPole = instant.atZone(ZoneId.of("Antarctica/South_Pole"))
val dst = southPole.zone.rules.isDaylightSavings(instant)
println("It is ${southPole.toLocalTime()} (UTC${southPole.offset}) at the South Pole")
println("The South Pole ${if (dst) "is" else "is not"} on Daylight Savings Time")