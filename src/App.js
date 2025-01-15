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
  const [searchId, setSearchId] = useState('');
  const [searchName, setSearchName] = useState('');
  const [deleteId, setDeleteId] = useState('');
  const [searchedUser, setSearchedUser] = useState(null);
  const [searchedUsersByName, setSearchedUsersByName] = useState([]);

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
        setUsers([...users, response.data]);
        setUser({
          id: '',
          name: '',
          address: '',
          phoneNumber: '',
          age: ''
        });
        window.location.reload();
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
        setUsers(users.map((u) => (u.id === id ? response.data : u)));
        setUser({
          id: '',
          name: '',
          address: '',
          phoneNumber: '',
          age: ''
        });
        setSelectedUserId(null);
        window.location.reload();
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
  };

  const handleDeleteUserById = () => {
    if (!deleteId) {
      alert('Please provide a User ID to delete');
      return;
    }

    axios.delete(`http://localhost:8080/users/${deleteId}`)
      .then(() => {
        alert('User deleted');
        setUsers(users.filter((user) => user.id !== deleteId));
        setDeleteId('');
        window.location.reload();
      })
      .catch((error) => {
        console.error('Error deleting user:', error);
        alert('There was an error deleting the user.');
      });
  };

  const handleSearchUserById = () => {
    if (!searchId) {
      alert('Please provide a User ID to search');
      return;
    }

    const userToSearch = users.find((user) => String(user.id) === String(searchId));
    if (userToSearch) {
      setSearchedUser(userToSearch);
    } else {
      alert('User not found');
      setSearchedUser(null);
    }
  };

  const handleSearchUserByName = () => {
    if (!searchName) {
      alert('Please provide a User Name to search');
      return;
    }

    const filteredUsers = users.filter((user) =>
      user.name.toLowerCase().includes(searchName.toLowerCase())
    );

    if (filteredUsers.length > 0) {
      setSearchedUsersByName(filteredUsers);
    } else {
      alert('No users found with that name');
      setSearchedUsersByName([]);
    }
  };

  const handleFetchAllUsers = () => {
    axios.get('http://localhost:8080/users')
      .then((response) => {
        setUsers(response.data);
        setIsUsersFetched(true);
       
      })
      .catch((error) => {
        console.error('There was an error fetching all users', error);
        alert('There was an error fetching the users.');
      });
  };

  const isEditMode = !!selectedUserId;

  return (
    <div className="App">
      <h1>User Management</h1>

      {loading ? (
        <div>Loading...</div>
      ) : (
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
        <div  className="user-list-container">
          <h2>User List</h2>
          <ul>
            {users.map((user) => (
              <li key={user.id}>
                <p> User Id:{user.id},{user.name} 
                <button onClick={() => handleSelectUser(user.id)}>Edit</button> </p>
               
              </li>
            ))}
          </ul>
        </div>
      )}
      <h3>Search by id</h3>
      <div>
        <input
          type="number"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
          placeholder="Search User by ID"
        />
        
        <button onClick={handleSearchUserById}>Search User</button>
        {searchedUser && (
          <div>
            <h3>Search Result</h3>
            <p>ID: {searchedUser.id}</p>
            <p>Name: {searchedUser.name}</p>
            <p>Address: {searchedUser.address}</p>
            <p>Phone: {searchedUser.phoneNumber}</p>
            <p>Age: {searchedUser.age}</p>
          </div>
        )}
      </div>
      <label >Search by id</label>
      <div>
        <input
          type="text"
          value={searchName}
          onChange={(e) => setSearchName(e.target.value)}
          placeholder="Search User by Name"
        />
        <button onClick={handleSearchUserByName}>Search Users by Name</button>
        {searchedUsersByName.length > 0 && (
          <div>
            <h3>Search Results by Name</h3>
            <ul>
              {searchedUsersByName.map((user) => (
                <li key={user.id}>
             <p>ID: {user.id}</p>
            <p>Name: {user.name}</p>
            <p>Address: {user.address}</p>
            <p>Phone: {user.phoneNumber}</p>
            <p>Age: {user.age}</p>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
      <label>Delete by id</label>
      <div>
        <input
          type="text"
          value={deleteId}
          onChange={(e) => setDeleteId(e.target.value)}
          placeholder="Delete User by ID"
        />
        <button onClick={handleDeleteUserById}>Delete User</button>
      </div>

      
    </div>
  );
}
//export NODE_OPTIONS=--openssl-legacy-provider
