package org.tecnalia.artist.util.file;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.tecnalia.structures.SourceFile;

public class ClassExplorer {
	
	private URLClassLoader classLoader;

	public void setPath(String path) throws Exception{
		classLoader = URLClassLoader.newInstance(new URL[] {new URL(path)});
	}
	
	public void findMethods(String className) throws Exception{

		Class<?> c = classLoader.loadClass(className);
		
	    System.out.println();
	    System.out.println("***** Methods in class "+className);
		for (Method method : c.getDeclaredMethods()) {
		    System.out.println(method.getName());
		}
		
	}

	public SourceFile findMethodsParser(File file) throws Exception{
		return findMethodsParser(file, null);
	}

	public SourceFile findMethodsParser(File file, ArrayList<Integer> lines) throws Exception{
		CompilationUnit cu = JavaParser.parse(file);
		//new MethodVisitor().visit(cu, null);
		SourceFile sf;
	    if(lines==null)
	    	sf =getMethods(cu);
	    else
	    	sf =getMethods(cu, lines);
	    	
	    sf.setName(file.getName());
	    sf.setPackageName(cu.getPackage().toString());
		return sf;

	}

	
	private SourceFile getMethods(CompilationUnit cu, ArrayList<Integer> lines) {
		SourceFile retorno= new SourceFile();
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
            		org.tecnalia.structures.Method obj= new org.tecnalia.structures.Method(); 
                    MethodDeclaration m = (MethodDeclaration) member;
                    obj.setName(m.getName());
                    //obj.setCode(m.toString());
                    obj.setBline(""+m.getBeginLine());
                    obj.setEline(""+m.getEndLine());
                    String reusable= "true";
                    for (Integer line: lines){
                    	if (line>m.getBeginLine()&&line<m.getEndLine()){
                    		reusable="false";
                    	}
                    }
                    	
                    obj.setReusable(reusable);
                    retorno.addLinea(obj);
                }
            }
        }
        return retorno;
    }
	
	private SourceFile getMethods(CompilationUnit cu) {
		SourceFile retorno= new SourceFile();
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
            		org.tecnalia.structures.Method obj= new org.tecnalia.structures.Method(); 
                    MethodDeclaration m = (MethodDeclaration) member;
                    obj.setName(m.getName());
                    obj.setCode(m.toString());
                    obj.setBline(""+m.getBeginLine());
                    obj.setEline(""+m.getEndLine());
                    obj.setReusable("true");
                    retorno.addLinea(obj);
                }
            }
        }
        return retorno;
    }
	

	public void findMethodsbyPath (File file) throws Exception{
		Scanner scanner = new Scanner(file);
		while(scanner.hasNext())
		{
			String line=scanner.nextLine();
			int lineNum=line.indexOf("{");
			
			if(lineNum>=0)
		    {
				lineNum=line.indexOf("(");
				if(lineNum>=0)
			    {
					String methodName=getMethodName(line);
					if (!methodName.equals(" ")){
						System.out.println("Linea: "+line);
						System.out.println(getMethodName(line));
					}
			    }
		    }
		}	
		
	}

	public void analyzeClass (File file) throws Exception{
		Scanner scanner = new Scanner(file);
		String line="";
		while(scanner.hasNext())
		{
			line=scanner.nextLine();
			int linePos=line.indexOf("package");
			
			if(linePos>=0)
		    {
				System.out.println("package: "+line.substring(8, line.length()-1));
				break;
		    }
		}	
		
		while(scanner.hasNext())
		{
			line=scanner.nextLine();
			int linePos=line.indexOf("import");
			int linePos2=line.indexOf("class");
			if (linePos2>=0){
				System.out.println("class: "+line.substring(linePos2+6,line.length()-1));
				break;
			}
			
			if(linePos>=0)
		    {
				System.out.println("import: "+line.substring(7, line.length()-1));
		    }
		}
		
	}
	
	
	
	private String getMethodName(String line){
		StringTokenizer token = new StringTokenizer(line, " ");
		String retorno="";
		int j=token.countTokens();
		for (int i=0;i<j;i++){
			retorno= token.nextToken();
			int lineNum=retorno.indexOf("(");
			if(lineNum>=0)
		    {
				retorno=retorno.substring(0, lineNum);
				break;
		    }
			
		}
		return retorno;
	}
	
	
}
