# Spear
Spear is a data saving format, written in Java. It's made to be super compact, not to be readable.
## Sample file
A Spear file looks like this:
```
(this:is:a:test:(so:can1:it23:handle111:this:(ehhh=-1238,i="don'tknow",canyoudothis="Hello\nHi\"Quote\"()\\n"),test:wow=true,does=true,it=1,work="hopefully?"),list1=[],list2=[1,2,"Hoi!"],list3=[key1="item1",key2=true])
```

And this is, represented in YAML:
```yaml
this:
  is:
    a:
      test:
        so:
          can1:
            it23:
              handle111:
                this:
                  ehhh: -1238
                  i: "don'tknow"
                  canyoudothis: "Hello\nHi\"Quote\"()\\n"
        test:
          wow: true
        does: true
        it: 1
        work: "hopefully?"
list1: []
list2: [1, 2, "Hoi!"]
list3:
  key1: "item1"
  key2: true
```
A lot more compact, right? But also a lot less readable.
## How does the format work
You have, just like in YAML, an identifier (the node) followed by a colon followed by a value/subnode.
Only if a node has multiple subnodes, they are separated by commas and placed in brackets.
Possible values are:
* Another node (subnode)
* Integer
* Double or float (converted to double when retrieving)
* String
* Boolean
* List (normally converted to an ArrayList when retrieving, but if it is key based converted to a HashMap)

If the value is another node, it is assigned by a colon, if it is any other, it is assigned by an equal sign.
In Spear, that is called an assignment.
## Known bugs
There is only one known bug: Spear is not able to parse empty key based lists (this is because in Spear the format of lists and key based lists is the same, if there are no objects Spear does not see any key so parses it as a normal list). This will not be worked on, users need to add a dummy object if their key based list is empty.
## Using the Spear API
You can load a Spear file (.sp file) using the SPData class (which represents a Spear file). You can also load from a string.
```java
SPData.load(file);
SPData.loadFromString(string);
```
These methods throw an `InvalidCharacterException` or `UnexpectedTokenException` if the Spear file could not be correctly parsed. If you load from a file it can throw a `FileNotFoundException` if the file was not found, and an `IncorrectFileTypeException` if the file was not a Spear file.

Saving is also easy:
```java
data.save(file);
```
This method creates also the file if it doesn't exist.
It throws an `IOException` if the file already existed as a directory or if there was an error while writing, and an `IncorrectFileTypeException` if the file was not a Spear file.

Setting values of the file:
```java
data.setInteger(path, integer);
data.setDouble(path, double);
data.setString(path, string);
data.setBoolean(path, boolean);
data.setList(path, arraylist);
data.setList(path, hashmap);
```
All these methods throw an InvalidPathException if the path contains characters that aren't supported.
You need to watch out when putting a HashMap into a Spear file, because Spear lists, if they are key based, use identifiers and not values as key.
So the key can only be a String. If otherwise, an UnsupportedKeyException will be thrown.

The paths work the same as in YAML, so if you want to set for example `wow` to false (in the sample file), you use the path `this.is.a.test.test.wow`.

Getting values from the file:
```java
data.getInteger(path);
data.getDouble(path);
data.getString(path);
data.getBoolean(path);
data.getList(path);
data.getKeyBasedList(path);
```
These methods return `null` when the path does not exist, or the result is not of the type you asked for.  
When retrieving lists, it is a little more tricky:
```java
ArrayList<Object> list = data.getList("path.to.list.which.is.not.key.based");
HashMap<String, Object> list2 = data.getKeyBasedList("path.to.list.which.is.key.based");
```
Yes indeed, in Spear, it's not sure what the type of a list is. So you can have a list like this:
```
[key1="item1",key2="item2",key3="item3",key4=true]
```
But if you store a HashMap, the key must always be a string. This is because Spear works with identifiers as key.

Now, you're done reading this. Go use Spear!  
Have fun with it :-)
