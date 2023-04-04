<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { Modal } from 'bootstrap'
import GlobalService from '../js/GlobalService.js'

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
    default: false
  },
  projectId: {
    type: String,
    required: true,
    default: ''
  }
})
const emit = defineEmits(['update:visible', 'save-new-projectdata'])

const modalRef = ref()
let modal = null
const updatedProject = reactive({
  id: '',
  name: '',
  description: '',
  owner: '',
  owner_ref: '',
  collaberators: [],
  active: true
})
const users = ref([])

onMounted(() => {
  if (modalRef.value) {
    modal = new Modal(modalRef.value)
    modalRef.value.addEventListener('hidden.bs.modal', close)
  }
})

watch(
  () => props.visible,
  (visible) => {
    if (visible) {
      GlobalService.prepareUpdateProject(
        props.projectId,
        onSuccessPrepareUpdateProject,
        onErrorPrepareUpdateProject
      )
    } else {
      modal.hide()
    }
  }
)

watch(
  () => updatedProject.owner_ref,
  (currentValue) => {
    let currentOwner = users.value.find(
      (element) => element.id === currentValue
    )
    updatedProject.owner = currentOwner.email
  }
)

function onSuccessUpdateProject(data) {
  if (data.status === 200) {
    emit('save-new-projectdata', updatedProject)
  } else if (data.status === 204) {
    emit('save-new-projectdata')
  }

  close()
}

function onErrorUpdateProject(error) {
  // TODO: display error on Toast
  console.log(error)
}

function onSuccessPrepareUpdateProject(data) {
  Object.assign(updatedProject, data.updateProject)
  users.value = data.users
    .map((user) => {
      return {
        id: user.id,
        email: user.email
      }
    })
    .sort((a, b) => {
      if (a.email > b.email) {
        return 1
      }
      if (a.email < b.email) {
        return -1
      }
      return 0
    })
  modal.show()
}

function onErrorPrepareUpdateProject(error) {
  // TODO: display error on Toast
  console.log(error)
}

function applyUpdateProject() {
  GlobalService.updateProject(
    updatedProject,
    onSuccessUpdateProject,
    onErrorUpdateProject
  )
}

function close() {
  emit('update:visible', false)
}

function onRemoveCollaberators(index) {
  updatedProject.collaberators.splice(index, 1)
}

function onChange(index) {
  const checkUsername = (obj) => obj.id === index.target.value

  // Check if this value is existing into collaberators
  if (!updatedProject.collaberators.some(checkUsername)) {
    let newUser = users.value.find(
      (element) => element.id === index.target.value
    )
    updatedProject.collaberators.push({ id: newUser.id, email: newUser.email })
  }
}
</script>

<template>
  <div ref="modalRef" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Project Edition</h5>
          <button
            type="button"
            class="btn-close"
            aria-label="Close"
            @click="close()"
          ></button>
        </div>
        <div class="modal-body">
          <form>
            <div class="mb-3">
              <label for="inputId" class="form-label">Id</label>
              <input
                id="inputId"
                :value="updatedProject.id"
                type="text"
                placeholder="Id"
                disabled
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label for="inputName" class="form-label">Name</label>
              <input
                id="inputName"
                v-model="updatedProject.name"
                type="text"
                class="form-control"
                placeholder="Name"
                @keydown.enter="applyUpdateProject"
              />
            </div>
            <div class="mb-3">
              <label for="inputDescription" class="form-label"
                >Description</label
              >
              <input
                id="inputDescription"
                v-model="updatedProject.description"
                type="text"
                class="form-control"
                placeholder="Description"
                @keydown.enter="applyUpdateProject"
              />
            </div>
            <div class="mb-3">
              <label for="owner" class="form-label">Owner</label>
              <select
                id="owner"
                v-model="updatedProject.owner_ref"
                type="text"
                placeholder="Id"
                class="form-control"
              >
                <option
                  v-for="option in users"
                  :key="option.id"
                  :value="option.id"
                >
                  {{ option.email }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <div class="form-check">
                <input
                  id="checkIsActive"
                  v-model="updatedProject.active"
                  class="form-check-input"
                  type="checkbox"
                />
                <label class="form-check-label" for="checkIsActive">
                  Is Active ?
                </label>
              </div>
            </div>
            <div class="mb-3">
              <label for="selectcollaberators" class="form-label"
                >Collaberators</label
              >
              <select
                id="selectcollaberators"
                type="text"
                placeholder="Id"
                class="form-control"
                @change="onChange"
              >
                <option value="undefined" disabled>Select your option</option>
                <option
                  v-for="option in users"
                  :key="option.id"
                  :value="option.id"
                >
                  {{ option.email }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <ul>
                <li
                  v-for="(item, index) in updatedProject.collaberators"
                  :key="item.id"
                >
                  <button
                    type="button"
                    class="btn btn-outline-danger ms-2 btn-sm"
                    @click="onRemoveCollaberators(index)"
                  >
                    <i class="bi bi-trash"></i>
                  </button>
                  {{ item.email }}
                </li>
              </ul>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close()">
            Close
          </button>
          <button
            type="button"
            class="btn btn-primary"
            @click="applyUpdateProject()"
          >
            Save changes
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
