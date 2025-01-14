import React, { useState, useEffect } from 'react';

import axios from 'axios';
import './App.css';

export default function UseForm() {
  const [user, setUser] = useState({
    id: '',
    name: '',
    address: '',
    phoneNumber: '',
    age: ''
  });
  const [users, setUsers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [isUsersFetched, setIsUsersFetched] = useState(false); 
  const [loading, setLoading] = useState(false); 
 
  useEffect(() => {
    setLoading(true); 
    axios
      .get('http://localhost:8080/users')
      .then((response) => {
        setUsers(response.data);
      })
      .catch((error) => {
        console.error('There was an error fetching users', error);
      })
      .finally(() => {
        setLoading(false); 
      });
  }, []);

  function handleInputChange(e) {
    const { name, value } = e.target;
    setUser({
      ...user,
      [name]: value
    });
  }

 
  function handleSubmit(e) {
    e.preventDefault();
    const { id, ...userWithoutId } = user;

    
    if (!user.name || !user.address || !user.phoneNumber || !user.age) {
      alert("All fields are required!");
      return;
    }

    
    if (id && selectedUserId) {
      handleUpdateUser(e);
    } else {
      
      handleCreateUser(e);
    }
  }

 
  function handleCreateUser(e) {
    axios.post('http://localhost:8080/users', user)
      .then((response) => {
        alert('User data submitted successfully');
        setUsers([...users, response.data]); // Add new user to list
        setUser({
          id: '',
          name: '',
          address: '',
          phoneNumber: '',
          age: ''
        });
      })
      .catch((error) => {
        console.error('There was an error submitting the user data!', error);
        alert('There was an error submitting the data.');
      });
  }

 
  function handleUpdateUser(e) {
    const { id, ...updateUser } = user;
    if (!id) {
      alert('Invalid user ID');
      return;
    }

    axios.put(`http://localhost:8080/users/${id}`, updateUser)
      .then((response) => {
        alert('User data updated');
        setUsers(users.map((u) => (u.id === id ? response.data : u))); // Update user in list
        setUser({
          id: '',
          name: '',
          address: '',
          phoneNumber: '',
          age: ''
        }); 
        setSelectedUserId(null); 
      })
      .catch((error) => {
        console.error('There was an error updating the user data', error);
        alert('There was an error updating the user.');
      });
  }


  function handleSelectUser(id) {
    const userToSelect = users.find((u) => u.id === id);
    if (userToSelect) {
      setUser(userToSelect); 
      setSelectedUserId(id); 
    }
  }


  const handleDeleteUser = (id) => {
    axios.delete(`http://localhost:8080/users/${id}`)
      .then(() => {
        alert('User deleted');
        setUsers(users.filter((user) => user.id !== id)); 
      })
      .catch((error) => {
        console.error('There was an error deleting the user', error);
        alert('There was an error deleting the user.');
      });
  };

  
  function handleFetchAllUsers() {
    axios.get('http://localhost:8080/users')
      .then((response) => {
        setUsers(response.data); 
        setIsUsersFetched(true); 
      })
      .catch((error) => {
        console.error('There was an error fetching all users', error);
        alert('There was an error fetching the users.');
      });
  }

  
  const isEditMode = !!selectedUserId;

  return (
    <div className="App">
      <h1>User Management</h1>

    {loading ?(
      <div> Loading ...</div>
    ):(
    
      <form onSubmit={handleSubmit}>
        <label>
          User ID:
          <input
            type="text"
            name="id"
            value={user.id}
            onChange={handleInputChange}
            placeholder="Enter User ID"
            disabled={isEditMode} 
          />
        </label>
        <br />
        <label>
          Name:
          <input
            type="text"
            name="name"
            value={user.name}
            onChange={handleInputChange}
            placeholder="Enter User Name"
          />
        </label>
        <br />
        <label>
          Address:
          <input
            type="text"
            name="address"
            value={user.address}
            onChange={handleInputChange}
            placeholder="Enter User Address"
          />
        </label>
        <br />
        <label>
          Phone Number:
          <input
            type="text"
            name="phoneNumber"
            value={user.phoneNumber}
            onChange={handleInputChange}
            placeholder="Enter User Phone Number"
          />
        </label>
        <br />
        <label>
          Age:
          <input
            type="number"
            name="age"
            value={user.age}
            onChange={handleInputChange}
            placeholder="Enter User Age"
          />
        </label>
        <br />
        <button type="submit">{isEditMode ? 'Update' : 'Submit'}</button>
      </form>
    )}
     
      <button onClick={handleFetchAllUsers}>Fetch All Users</button>


      {isUsersFetched && (
        <div>
          <h2>User List</h2>
          <ul>
            {users.map((user) => (
              <li key={user.id}>
                {user.name} - {user.phoneNumber}
                <button onClick={() => handleSelectUser(user.id)}>Edit</button>
                <button onClick={() => handleDeleteUser(user.id)}>Delete</button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
