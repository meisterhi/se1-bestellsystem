# Project: *se1-bestellsystem*

Project of a simple order processing system for the *Software Engineering-I*
course.

Eine Klasse *Customer* hat Attribute:

- *id* (long) to identify the entity,

- *lastName* and *firstName* (String) für Namen sowie

- contacts (List<String>) als Liste der Kontakte (z.B. email, phone).

Die Klassen *Order* und *Article* haben ebenso *id*‐Attribute zur
Identifikation.

<img src="https://raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/main/concept-diagram.png" alt="drawing" width="600"/>

**Kardinalitäten (Anzahlmaße)** an den Beziehungen zwischen den Klassen
beschreiben die Zuordnungen zwischen den Objekten der jeweiligen Klassen.

Ein Kunde kann mehrere Bestellungen haben, d.h. die Kardinalität der Beziehung
zwischen Customer und Order ist `[ 1 : * ]`. Der Stern bedeutet mehrere,
einschließlich Null. Die Kardinalität steht bei der Klasse, für die sie gilt,
d.h. hier `*` bei Order. Fehlt die Angabe (wie auf der Seite von Customer),
gilt `1`.

Eine Bestellung muss mindestens eine (oder mehrere) Bestellpositionen
(bestellte Artikel) enthalten.
Daher besteht zwischen den Klassen Order und OrderItem die Kardinalität:
`[ 1 : 1..* ]`.

Jede Bestellposition bezieht sich genau auf einen Artikel, aber nicht alle
Artikel müssen auch in Bestellpositionen vorkommen.
Daher gilt zwischen den Klassen OrderItem und Article die Kardinalität
`[ * : 1 ]`.

**Aggregation (weißer Diamant)** drückt die logische Zugehörigkeit (*"ownership"*)
von Bestellungen zu Kunden aus, d.h. Bestellungen sind stets Kunden zugeordnet.
Bestellungen können nicht ohne Kunden existieren, sind aber nicht Teil der
Klasse Customer. Die Umsetzung dieser Beziehung wird mit einen Rückbezug über das
Attribut customer in der Klasse Order hergestellt. Die Klasse Customer selbst hat
keine Information über Bestellungen. Jede Bestellung kennt aber den zugehörigen
Kunden (Eigentümer oder *"owner"*) über das Attribut customer.
Bestellungen ohne Kunden kann es nicht geben.

**Komposition (schwarzer Diamant)** bedeutet, dass Bestellpositionen Bestandteil
(*"part_of"*) der Klasse Order sind. Die Klasse Order enthält in diesem Fall eine
Liste mit Bestellposition durch das Attribut items.

Über die Listenzugehörigkeit gehören Bestellpositionen implizit zu einer Bestellung.
Die Zugehörigkeit zu Bestellungen muss daher nicht in der Klasse OrderItem vermerkt
werden. Objekte der Bestelpositionen sind über die items‐Liste je einer Bestellung
zugeordnet. Für sich allein sind sie nicht zuordenbar.

Besteht keine spezielle *"ownership"* oder *"part_of"* – Beziehung zwischen den
Klassen, wird eine einfache Relation ohne Diamond verwendet, wie zwischen den
Klassen *OrderItem* und *Article*. Ein Artikel existiert unabhängig von Bestellungen.
Mehrere Bestellpositionen können auf je einen Artikel verweisen `( * : 1 )`.


