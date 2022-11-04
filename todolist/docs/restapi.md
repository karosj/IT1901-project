# Todo list API

## Base endpoint

Methods:

* GET - retrieves a list of todo lists from the server
  * URI: host:port/todo
  <http://localhost:8080/todo>
  * parameters: none
  * available: jetty, springboot
  * returns JSON with the todo lists

```json
{
  "lists":[
    {"name":"todo1"},
    {"name":"todo2"}
  ]
}
```

## list

<http://localhost:8080/todo/list/{name}>

Methods:

* GET - retrieves the specified list content from the server
  * URI: host:port/todo/list/{name}
 <http://localhost:8080/todo/list/todo1>
  * parameters: none
  * available: jetty, springboot
  * returns JSON with the todo list content

```json
{
  "name":"todo1",
  "items":[
   {"text":null,"checked":false}
  ]
}
```

* PUT creates a list with the desired name if it does not exist or updates the existing list otherwise
  * URI: host:port/todo/list/{name}
  * parameters:
    * body -  application/json; charset=UTF-8
    * list content

```json
{
  "name":"todo3",
  "items":[
        { "text":"item3" , "checked":true }
   ]
}
```

* available: jetty, springboot
* returns
  * Content-Type: application/json
  * json with boolean true on success

```json
   true
```
  
* DELETE - delete the list with the name {name}
  * URI: host:port/todo/list/{name}
  * parameters: none
  * available: jetty, springboot
  * returns
    * Content-Type: application/json
    * json with boolean true on success

```json
   true
```

* POST - rename a list
  * URI: host:port/todo/list/{name}/rename
  * parameters - form / body
    * application/x-www-form-urlencoded; charset=UTF-8
    * newName={newName}
  * available: jetty, springboot
  * returns
    * Content-Type: application/json
    * json with boolean true on success

```json
   true
```

## settings

<http://localhost:8080/todo/settings>

* the fxui client is not sending currently any requests for the settings

Methods:

* GET - retrieves the settings
  * URI: host:port/todo/settings
 <http://localhost:8080/todo/settings>
  * parameters: none
  * available: jetty
  * returns JSON with the settings content

```json
    {"todoItemsSortOrder":"NONE"}
```

* PUT - replaces the settings
  * URI: host:port/todo/settings
 <http://localhost:8080/todo/settings>
  * parameters:
    * body -  application/json; charset=UTF-8
    * list content

```json
    {"todoItemsSortOrder":"CHECKED_UNCHECKED"}
```

* todoItemsSortOrder can take the following values:
  * NONE
  * CHECKED_UNCHECKED
  * UNCHECKED_CHECKED
* If any other values are sent the todoItemsSortOrder will be set to *NONE*
* available: jetty
  * returns JSON with the settings content

```json
    {"todoItemsSortOrder":"NONE"}
```
