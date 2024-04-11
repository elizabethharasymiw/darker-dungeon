target: 
	javac com/mypackage/Menu.java com/mypackage/Map.java com/mypackage/DarkerDungeon.java

run: com/mypackage/DarkerDungeon.class
	java com.mypackage.DarkerDungeon

clean:
	rm ./com/mypackage/*.class
