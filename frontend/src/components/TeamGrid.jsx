import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import axios from "axios";
import {DataGrid} from "@mui/x-data-grid";
import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Skeleton, Stack, TextField} from "@mui/material";
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline';
import {useCallback, useState} from "react";

const TeamGrid = () => {
    const cols = [
        {field: 'id', headerName: 'ID', width: 70},
        {field: 'name', headerName: 'Nom', width: 125, editable: true},
        {field: 'slogan', headerName: 'Slogan', width: 125, editable: true},
        {
            field: 'delete',
            headerName: 'Delete',
            width: 120,
            renderCell: (params) => (
                <Button variant="outlined" color="error" onClick={() => del(params.row.id)}>
                    <DeleteOutlineIcon/>
                </Button>
            ),
        },
    ]
    const queryClient = useQueryClient()
    const usePut = () => {
        return useMutation({
            mutationFn: async ({id, ...body}) => {
                const {data} = await axios.put("http://localhost:8000/teams/" + id, body)
                return data
            },
            onSuccess: () => queryClient.invalidateQueries('getTeams')
        })
    }
    const useDelete = () => {
        return useMutation({
            mutationFn: async (id) => {
                const {data} = await axios.delete("http://localhost:8000/teams/" + id)
                return data
            },
            onSuccess: () => queryClient.invalidateQueries('getTeams')
        })
    }
    const usePost = () => {
        return useMutation({
            mutationFn: async (body) => {
                const {data} = await axios.post("http://localhost:8000/teams", body)
                return data
            },
            onSuccess: () => queryClient.invalidateQueries('getTeams')
        })
    }
    const useGet = () => {
        return useQuery({
            queryKey: ["getTeams"],
            queryFn: async () => {
                const {data} = await axios.get("http://localhost:8000/teams")
                return data
            },
        })
    }
    const { data, isLoading} = useGet()
    const {mutate: edit, isPending: isLoadput} = usePut()
    const {mutate: create, isPending: isLoadpost} = usePost()
    const {mutate: del, isPending: isLoadel} = useDelete()
    const [openDialog, setOpenDialog] = useState(false)
    const [newTeam, setNewTeam] = useState({name: '', slogan: ''})
    const editHandler = (params, event) => {
        const {field, row} = params
        const value = event.target.value;
        console.log(field, row, value)
        edit({...row, [field]: value})
    }

    const onCloseModal = () => {
        setOpenDialog(false)
        setNewTeam({name: '', slogan: ''})
    }
    const createHandler = useCallback(() => {
        create(newTeam)
        onCloseModal()
    }, [create, newTeam])

    if (isLoading || isLoadput || isLoadpost || isLoadel) {
        return <Skeleton animation="wave"/>
    }

    return <>
        <Stack direction="row" spacing={1} sx={{mb: 1}}>
            <Button size="small" onClick={() => setOpenDialog(true)}>Add a row</Button>
        </Stack>
        <DataGrid columns={cols} rows={data} onCellEditStop={editHandler}/>
        <Dialog open={openDialog} onClose={onCloseModal}>
            <DialogTitle>Create a New Team</DialogTitle>
            <DialogContent>
                <TextField
                    label="Team Name"
                    fullWidth
                    margin="normal"
                    variant="outlined"
                    onChange={(e) => setNewTeam(() => ({...newTeam, name: e.target.value}))}
                />
                <TextField
                    label="Slogan"
                    fullWidth
                    margin="normal"
                    variant="outlined"
                    onChange={(e) => setNewTeam(() => ({...newTeam, slogan: e.target.value}))}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onCloseModal} color="secondary">
                    Cancel
                </Button>
                <Button onClick={createHandler} color="primary" variant="contained">
                    Create Team
                </Button>
            </DialogActions>
        </Dialog>
    </>
}

export default TeamGrid