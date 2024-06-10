import { NgFor } from '@angular/common';
import { Component } from '@angular/core';
import { DataTablesModule } from 'angular-datatables';
import { ticketsData } from './ticketsData';

@Component({
  selector: 'app-tickets',
  standalone: true,
  imports: [DataTablesModule,NgFor],
  templateUrl: './tickets.component.html',
  styleUrl: './tickets.component.css'
})
export class TicketsComponent {
  tickets=ticketsData;
}
