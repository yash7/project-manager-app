package ateamcomp354.projectmanagerapp.jooq;


import ateamcomp354.projectmanagerapp.dataAccess.DatabaseManager;
import org.jooq.util.jaxb.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

// This uses jooq to generate java code that models the database.
public class GenerationTool {

	public static void main(String[] args) throws Exception {
		
		 // first, create/update the database schema
		new DatabaseManager();

		Configuration c = org.jooq.util.GenerationTool.load( new FileInputStream( "jooq-code.xml" ) );

		deleteTarget( c ); // delete old generated code, just to be safe.

		// use jooq to generate sql code for us!
		org.jooq.util.GenerationTool.generate( c );
	}

	private static void deleteTarget( Configuration c ) throws Exception {

		String dir = c.getGenerator().getTarget().getDirectory();
		String p = c.getGenerator().getTarget().getPackageName();

		File target = new File( dir,  p.replace( '.', '/' ) );
		Path targetPath = target.toPath();

		Files.walkFileTree( targetPath, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				file.toFile().delete();
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				if ( !targetPath.equals( dir ) ) {
					dir.toFile().delete();
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
}
