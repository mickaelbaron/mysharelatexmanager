const APP_SERVER_URL = import.meta.env.VITE_APP_SERVER_URL

function getFakeUsers(onSuccess, onError) {
  let request = new Request('/users.json', {
    method: 'GET'
  })

  globalFech(true, request, onSuccess, onError)
}

function getFakeProjects(onSuccess, onError) {
  let request = new Request('/projects.json', {
    method: 'GET',
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function getCommonHeaders() {
  return {
    'Content-Type': 'application/json',
    Authorization: localStorage.getItem('TOKEN')
  }
}

function updateUser(user, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/users', {
    method: 'PUT',
    body: JSON.stringify(user),
    headers: getCommonHeaders()
  })

  globalFech(false, request, onSuccess, onError)
}

function getProjects(onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/projects', {
    method: 'GET',
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function getUsers(onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/users', {
    method: 'GET',
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function prepareTransfertUserProjects(userId, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/users/transfert/' + userId, {
    method: 'GET',
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function transfertUserProjects(transfertUserProjects, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/users/transfert', {
    method: 'PUT',
    body: JSON.stringify(transfertUserProjects),
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function getAuthenticationLogin(credentials, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/authentication/login', {
    method: 'POST',
    body: JSON.stringify(credentials),
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function removeCollaberator(userId, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/users/collaberator/' + userId, {
    method: 'DELETE',
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function deleteUser(userId, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/users/' + userId, {
    method: 'DELETE',
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function prepareUpdateProject(projectId, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/projects/' + projectId, {
    method: 'GET',
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(true, request, onSuccess, onError)
}

function updateProject(project, onSuccess, onError) {
  let request = new Request(APP_SERVER_URL + '/projects', {
    method: 'PUT',
    body: JSON.stringify(project),
    Authorization: localStorage.getItem('TOKEN'),
    headers: getCommonHeaders()
  })

  globalFech(false, request, onSuccess, onError)
}

function globalFech(isGetMethod, request, onSuccess, onError) {
  fetch(request)
    .then((response) => {
      if (response.ok) {
        if (isGetMethod) {
          return response.json()
        } else {
          return response
        }
      }
      return Promise.reject(response.status)
    })
    .then((data) => {
      onSuccess(data)
    })
    .catch((error) => {
      onError(error)
    })
}

export default {
  deleteUser,
  getAuthenticationLogin,
  getProjects,
  getUsers,
  prepareTransfertUserProjects,
  prepareUpdateProject,
  removeCollaberator,
  transfertUserProjects,
  updateProject,
  updateUser,
  getFakeUsers,
  getFakeProjects
}
