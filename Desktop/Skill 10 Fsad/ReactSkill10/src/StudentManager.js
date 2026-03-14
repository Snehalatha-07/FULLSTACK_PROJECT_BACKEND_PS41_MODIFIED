import React, { useState } from 'react';
import './StudentManager.css';

function StudentManager() {

  const [students, setStudents] = useState([
    { id: 1, name: 'Snehalatha', course: 'Full Stack Development' },
    { id: 2, name: 'Ravi Kumar', course: 'Computer Science' },
    { id: 3, name: 'Priya Sharma', course: 'Data Science' },
    { id: 4, name: 'Anjali Reddy', course: 'Artificial Intelligence' },
    { id: 5, name: 'Kiran Babu', course: 'Cyber Security' }
  ]);

  const [newStudent, setNewStudent] = useState({
    id: '',
    name: '',
    course: ''
  });

  const handleChange = (e) => {
    setNewStudent({ ...newStudent, [e.target.name]: e.target.value });
  };

  const addStudent = () => {
    if (newStudent.id === '' || newStudent.name === '' || newStudent.course === '') {
      alert('Please fill all fields!');
      return;
    }
    setStudents([...students, newStudent]);
    setNewStudent({ id: '', name: '', course: '' });
  };

  const deleteStudent = (id) => {
    setStudents(students.filter(student => student.id !== id));
  };

  return (
    <div className="container">
      <h1>Student Manager</h1>
      <h3>Online Academic Portal</h3>

      <div className="form-section">
        <h2>Add New Student</h2>
        <input
          type="number"
          name="id"
          placeholder="Enter Student ID"
          value={newStudent.id}
          onChange={handleChange}
        />
        <input
          type="text"
          name="name"
          placeholder="Enter Student Name"
          value={newStudent.name}
          onChange={handleChange}
        />
        <input
          type="text"
          name="course"
          placeholder="Enter Course"
          value={newStudent.course}
          onChange={handleChange}
        />
        <button className="add-btn" onClick={addStudent}>Add Student</button>
      </div>

      <div className="list-section">
        <h2>Student List</h2>
        {students.length === 0 ? (
          <p className="empty-msg">No students available</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Course</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {students.map((student) => (
                <tr key={student.id}>
                  <td>{student.id}</td>
                  <td>{student.name}</td>
                  <td>{student.course}</td>
                  <td>
                    <button
                      className="delete-btn"
                      onClick={() => deleteStudent(student.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <div className="count-section">
        <p>Total Students: <strong>{students.length}</strong></p>
      </div>
    </div>
  );
}

export default StudentManager;
