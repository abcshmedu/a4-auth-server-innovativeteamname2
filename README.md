# 2. Pratkikumsaufgabe Software-Architektur Sommer 2017

Developer: Gabl, Daniel<br />
Deployment: [Heroku](https://innovative-teamname.herokuapp.com/) (not working yet)<br />
Project Status: 100%<br />


URI-Template|Verb|Wirkung
 -|-|-
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
**Bücher**| | 
 &nbsp;|author|String
 &nbsp;|isbn|String
 &nbsp;|title|String
**Discs**| |
 &nbsp;|barcode|String
 &nbsp;|director|String
 &nbsp;|fsk|Integer
 &nbsp;|title|String
