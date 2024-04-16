target:
	javac com/mypackage/GameState.java com/mypackage/Map.java com/mypackage/DarkerDungeon.java

run: target
	java com.mypackage.DarkerDungeon

release: target
	jar cfm DarkerDungeon.jar manifest.txt com/mypackage/*.class

release-test: release
	java -jar DarkerDungeon.jar

clean:
	rm ./com/mypackage/*.class
	rm DarkerDungeon.jar
