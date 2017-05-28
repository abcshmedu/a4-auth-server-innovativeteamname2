# 4. Pratkikumsaufgabe Software-Architektur Sommer 2017

Developer: Gabl, Daniel<br />
Deployment: [Heroku](https://innovative-teamname-auth.herokuapp.com/)<br />
Project Status: 100%<br />


URI-Template|Verb|Wirkung
 -|-|-
**User**| | 
/users|POST|Neuen User anlegen<br />Möglicher Fehler: User existiert bereits<br />Möglicher Fehler: Name und / oder Passwort vergessen
/users/login|POST|Einloggen und damit neuen OAuth-Token generieren
/users|GET|Alle User auflisten
/users|PUT|User-Daten modifizieren (automatische Prüfung ob User in Service-Routine existiert)<br />Möglicher Fehler: User nicht gefunden<br />Möglicher Fehler: Name und Passwort fehlen<br />Möglicher Fehler: Neue Daten entsprechen den alten Daten
/users/{name}|GET|Bestimmten User-Namen suchen
**Bücher**| | 
/media/books|POST|Neues Medium 'Buch' anlegen<br />Möglicher Fehler: Ungültige ISBN<br />Möglicher Fehler: ISBN bereits vorhanden<br />Möglicher Fehler: Autor oder Titel fehlt
/media/books/{isbn}|GET|Eine JSON-Repräsentation eines gespeicherten Buches liefern, falls vorhanden
/media/books|GET|Alle Bücher auflisten
/media/books|PUT|Daten modifizieren (automatische Prüfung ob ISBN in Service-Routine existiert)<br />Möglicher Fehler: ISBN nicht gefunden<br />Möglicher Fehler: Autor und Titel fehlen<br />Möglicher Fehler: Neue Daten entsprechen den alten Daten
**Discs**| |
/media/discs|POST|Neues Medium 'Disc' anlegen<br />Möglicher Fehler: Ungültiger Barcode<br />Möglicher Fehler: Barcode bereits vorhanden<br />Möglicher Fehler: Director oder Titel fehlt
/media/discs|GET|Alle Discs auflisten
/media/discs/{barcode}|GET|Eine JSON-Repräsentation einer gespeicherten Disc liefern, falls vorhanden
/media/discs|PUT|Daten modifizieren (automatische Prüfung ob Barcode in Service-Routine existiert)<br />Möglicher Fehler: Barcode nicht gefunden<br />Möglicher Fehler: Director, FSK und Titel fehlen<br />Möglicher Fehler: Neue Daten entsprechen den alten Daten


Object|Parameter|Input
 -|-|-
**User**| |
 &nbsp;|name|String
 &nbsp;|pass|String
 &nbsp;|role|String ("USER", "ADMIN" or "ROOT")
**Bücher**| | 
 &nbsp;|author|String
 &nbsp;|isbn|String
 &nbsp;|title|String
**Discs**| |
 &nbsp;|barcode|String
 &nbsp;|director|String
 &nbsp;|fsk|Integer
 &nbsp;|title|String