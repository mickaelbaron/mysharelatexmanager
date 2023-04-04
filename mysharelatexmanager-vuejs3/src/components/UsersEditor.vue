<script setup>
import { ref, onMounted, reactive, watch, inject } from 'vue'
import { FilterService, FilterMatchMode } from 'primevue/api'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'

import DetailUserRow from './DetailUserRow.vue'
import DetailUserDialog from './DetailUserDialog.vue'
import DeleteUserDialog from './DeleteUserDialog.vue'
import TransfertOwnerProjectsDialog from './TransfertOwnerProjectsDialog.vue'
import RemoveCollaboratorDialog from './RemoveCollaberatorDialog.vue'

import GlobalService from '../js/GlobalService.js'
import CommonUtils from '../js/CommonUtils.js'

const errorState = inject('ErrorState')

const props = defineProps({
  searchContent: {
    type: String,
    required: true,
    default: ''
  }
})

const allUsers = ref([])
const detailUserDialogVisible = ref(false)
const deleteUserDialogVisible = ref(false)
const transfertOwnerProjectsDialogVisible = ref(false)
const removeCollaberatorDialogVisible = ref(false)
const loading = ref(true)
const expandedRows = ref([])
const dateFilter = ref('date filter')

const currentUserEdit = reactive({
  id: '',
  first_name: '',
  last_name: '',
  institution: '',
  email: '',
  loginCount: '',
  lastLoggedIn: '',
  hashedPassword: '',
  isAdmin: true
})

const globalFilter = ref({
  global: { value: null, matchMode: FilterMatchMode.CONTAINS },
  id: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  first_name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  last_name: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  email: { value: null, matchMode: FilterMatchMode.STARTS_WITH },
  loginCount: { value: null, matchMode: FilterMatchMode.EQUALS },
  lastLoggedIn: { value: null, matchMode: FilterMatchMode.STARTS_WITH }
})

function onSuccessUsers(data) {
  allUsers.value = data.data
  loading.value = false
}

function onErrorUsers(error) {
  errorState.methods.setErrorCode(error)
}

watch(
  () => props.searchContent,
  (searchContent) => {
    globalFilter.value.global.value = searchContent
  }
)

onMounted(() => {
  loading.value = true
  GlobalService.getUsers(onSuccessUsers, onErrorUsers)

  FilterService.register(dateFilter.value, (value, filter) => {
    if (filter === undefined || filter === null || filter.trim() === '') {
      return true
    }

    if (value === undefined || value === null) {
      return false
    }

    // Only Last Updated is an number type
    if (typeof value === 'number') {
      let result = CommonUtils.displayDate(value)

      return result.toString().includes(filter.toString())
    }

    return value.toString().includes(filter.toString())
  })
})

function saveNewUserData(data) {
  if (data) {
    const objectToReplace = allUsers.value.find(
      (currentUser) => currentUser.id === data.id
    )

    if (objectToReplace) {
      Object.assign(objectToReplace, data)
    }
  }
}

function selectCurrentUserEdit(data) {
  currentUserEdit.id = data.id
  currentUserEdit.first_name = data.first_name
  currentUserEdit.last_name = data.last_name
  currentUserEdit.institution = data.institution
  currentUserEdit.email = data.email
  currentUserEdit.loginCount = data.loginCount
  currentUserEdit.lastLoggedIn = data.lastLoggedIn
  currentUserEdit.isAdmin = data.isAdmin
}

function openRemoveCollaberatorDialog(data) {
  selectCurrentUserEdit(data)
  removeCollaberatorDialogVisible.value = true
}

function openTransfertOwnerProjectsDialog(data) {
  selectCurrentUserEdit(data)
  transfertOwnerProjectsDialogVisible.value = true
}

/*function openDeleteUserDialog(data) {
  selectCurrentUserEdit(data)
  deleteUserDialogVisible.value = true
}*/

function openUserDetailDialog(data) {
  selectCurrentUserEdit(data)
  detailUserDialogVisible.value = true
}
</script>

<template>
  <RemoveCollaboratorDialog
    v-model:visible="removeCollaberatorDialogVisible"
    :user="currentUserEdit"
  />

  <DeleteUserDialog
    v-model:visible="deleteUserDialogVisible"
    :user="currentUserEdit"
  />

  <DetailUserDialog
    v-model:visible="detailUserDialogVisible"
    :user="currentUserEdit"
    @save-new-userdata="saveNewUserData"
  />

  <TransfertOwnerProjectsDialog
    v-model:visible="transfertOwnerProjectsDialogVisible"
    :user="currentUserEdit"
  />

  <DataTable
    v-model:expandedRows="expandedRows"
    v-model:filters="globalFilter"
    filter-display="row"
    :paginator="true"
    :rows="100"
    :rows-per-page-options="[10, 20, 50, 100]"
    :value="allUsers"
    data-key="id"
    current-page-report-template="Showing {first} to {last} of {totalRecords}"
    paginator-template="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
    :global-filter-fields="['id', 'first_name', 'last_name', 'email']"
    :loading="loading"
  >
    <template #loading> Loading customers data. Please wait. </template>
    <Column :expander="true" header-style="width: 3rem" />
    <Column field="id" :sortable="true" header="Id">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Id - ${filterModel.matchMode}`"
          aria-label="id"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="first_name" :sortable="true" header="First Name">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by First name - ${filterModel.matchMode}`"
          aria-label="first name"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="last_name" :sortable="true" header="Last Name">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Last name - ${filterModel.matchMode}`"
          aria-label="last name"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column field="email" header="Email" :sortable="true">
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          :placeholder="`Search by Email - ${filterModel.matchMode}`"
          aria-label="email"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column
      field="loginCount"
      header="Login Count"
      data-type="numeric"
      :sortable="true"
    >
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="Numeric"
          aria-label="loginCount"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column
      field="lastLoggedIn"
      header="Last Logged In"
      :sortable="true"
      :show-filter-menu="false"
      ><template #body="slotProps">{{
        CommonUtils.displayDate(slotProps.data.lastLoggedIn)
      }}</template>
      <template #filter="{ filterModel, filterCallback }">
        <input
          v-model="filterModel.value"
          type="text"
          class="form-control"
          placeholder="YYYY/MM/D HH:mm:ss"
          aria-label="lastUpdated"
          aria-describedby="basic-addon1"
          @change="filterCallback()"
        />
      </template>
    </Column>
    <Column style="min-width: 8rem">
      <template #body="slotProps">
        <div class="d-flex flex-nowrap">
          <button
            type="button"
            class="btn btn-outline-primary"
            :title="'Edit the content of ' + slotProps.data.email"
            @click="openUserDetailDialog(slotProps.data)"
          >
            <i class="bi bi-pencil"></i>
          </button>
          <button
            type="button"
            class="btn btn-outline-primary ms-2"
            :title="
              'Transfert the projects of ' +
              slotProps.data.email +
              ' to another user'
            "
            @click="openTransfertOwnerProjectsDialog(slotProps.data)"
          >
            <i class="bi bi-shuffle"></i>
          </button>
          <button
            type="button"
            class="btn btn-outline-danger ms-2"
            :title="
              'Remove collaberator (' +
              slotProps.data.email +
              ') of all projects'
            "
            @click="openRemoveCollaberatorDialog(slotProps.data)"
          >
            <i class="bi bi-person-fill-dash"></i>
          </button>
          <!--<button
            type="button"
            class="btn btn-outline-danger ms-2"
            :title="'Delete ' + slotProps.data.email"
            @click="openDeleteUserDialog(slotProps.data)"
          >
            <i class="bi bi-trash"></i>
          </button>-->
        </div>
      </template>
    </Column>
    <template #expansion="slotProps">
      <DetailUserRow :data="slotProps.data" />
    </template>
  </DataTable>
</template>
