package com.xml.apache.digester;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.digester.Digester;
import org.junit.Test;
import org.xml.sax.SAXException;

public class Academyparser {
	public void addCourse(){
		
	}
	
	public void addStudent(){
		
	}
	
	public void addCertification(){
		
	}
	
	public void addTeacher(){
		
	}
	
	@Test
	// <certification>在程序中对应一个字符串，故而没有列出。
	public void digest() {
		Digester digester = new Digester();
		// 注意，此处并没有象上例一样使用push，是因为此处从根元素创建了一个对//象实例
		digester.addObjectCreate("academy", Academy.class);
		// 将< academy >的属性与对象的属性关联
		digester.addSetProperties("academy");
		digester.addObjectCreate("academy/student", Student.class);
		digester.addSetProperties("academy/student");
		digester.addObjectCreate("academy/student/course", Course.class);
		digester.addBeanPropertySetter("academy/student/course/id");
		digester.addBeanPropertySetter("academy/student/course/name");
		digester.addSetNext("academy/student/course", "addCourse");
		digester.addSetNext("academy/student", "addStudent");
		digester.addObjectCreate("academy/teacher", Teacher.class);
		digester.addSetProperties("academy/teacher");
		// 当遇到academy/teacher/certification时，调用addCertification
		digester.addCallMethod("academy/teacher/certification",
				"addCertification", 1);
		// 设置addCertification的参数值，此处的0表示这个元素体的第一个值
		// 为参数值传入addCertification。在此处，即为<certification>的值。
		// （因为就只有一个）
		digester.addCallParam("academy/teacher/certification", 0);
		digester.addSetNext("academy/teacher", "addTeacher");
		try {
			Academy a = (Academy) digester.parse(new File("JavaSE/com/xml/apache/digester/xml.academy.xml"));
			System.out.print(a);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Academy {
	private String name;
	private Vector<Student> students;
	private Vector<Teacher> teachers;

	@Override
	public String toString() {
		return "Academy [name=" + name + ", students=" + students
				+ ", teachers=" + teachers + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector<Student> getStudents() {
		return students;
	}

	public void setStudents(Vector<Student> students) {
		this.students = students;
	}

	public Vector<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(Vector<Teacher> teachers) {
		this.teachers = teachers;
	}

}

class Student {
	private String name;
	private String division;
	private Vector<Course> courses;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public Vector<Course> getCourses() {
		return courses;
	}

	public void setCourses(Vector<Course> courses) {
		this.courses = courses;
	}

}

class Course {
	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

class Teacher {
	private String name;
	private Vector certifications;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector getCertifications() {
		return certifications;
	}

	public void setCertifications(Vector certifications) {
		this.certifications = certifications;
	}

}