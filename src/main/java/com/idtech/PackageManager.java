package com.idtech;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class PackageManager {

	/** 
	 * Given a package name, attempts to reflect to find all classes within the package 
	 * on the local file system. This will cause them to be loaded by the class loader.
	 *  
	 * @param packageName 
	 * @return A set of all of the classes found in the specified package.
	 */  
	public static Set<Class> loadClassesInPackage(String packageName) {  
		Set<Class> classes = new HashSet<Class>();  
		String packageNameSlashed = packageName.replace('.', '/');
		// Get a File object for the package  
		System.out.println("Loading Classes in Package: " + packageName);

		URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlashed);  

		if (directoryURL == null) {  
			System.out.println("Could not retrieve URL resource: " + packageNameSlashed);  
			return classes;  
		}  

		String directoryString = directoryURL.getFile();  
		System.out.println("Directory String: " + directoryString);



		if (directoryString == null) {  
			System.out.println("Could not find directory for URL resource: " + packageNameSlashed);  
			return classes;  
		}  


		try {

			// Make sure to create a file system in case of zip file.
			Map<String, String> env = new HashMap(); 
			env.put("create", "true");

			Path directoryPath;
			try {
				directoryPath = Paths.get(directoryURL.toURI());
			} catch (FileSystemNotFoundException e) {
				FileSystem fs = FileSystems.newFileSystem(directoryURL.toURI(), env);
				directoryPath = Paths.get(directoryURL.toURI());
				System.out.println("Opened Jar File Filesystem");
			}



			Stream<Path> directoryWalker = Files.walk(directoryPath);

			for(Iterator<Path> it = directoryWalker.iterator(); it.hasNext();){
				String fileName = it.next().getFileName().toString();
				System.out.println("File Name: " + fileName);
				//fileName = fileName.replace('\\','.');
				// We are only interested in .class files  
				if (fileName.endsWith(".class")) {  
					// Remove the .class extension  
					fileName = fileName.substring(0, fileName.length() - 6);  
					try {  
						classes.add(Class.forName(packageName + "." + fileName));  
					} catch (ClassNotFoundException e) {  
						System.out.println(packageName + "." + fileName + " does not appear to be a valid class.");  
					}  
				}  
			}



			directoryWalker.close();

		}
		catch( URISyntaxException e) {
			System.out.println("Could not convert " + directoryURL + " to a file.");
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return classes;  
	} 

	void walkJar(String path, URI uri){

	}

}
