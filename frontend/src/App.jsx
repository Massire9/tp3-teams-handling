import logo from './logo.svg';
import './App.css';
import React from "react";
import { DataGrid } from '@mui/x-data-grid';
import {QueryClient, QueryClientProvider, useQuery} from "@tanstack/react-query";
import axios from "axios";
import TeamGrid from "./components/TeamGrid";
const queryClient = new QueryClient()

function App() {


  return (
      <QueryClientProvider client={queryClient}>
          <TeamGrid />
      </QueryClientProvider>
  );
}

export default App;
