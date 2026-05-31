import { Routes } from '@angular/router';
import { BarracaListComponent } from './components/barraca-list/barraca-list.component';
import { BarracaFormComponent } from './components/barraca-form/barraca-form.component';

export const routes: Routes = [
  { path: 'barracas', component: BarracaListComponent },
  { path: 'barracas/nova', component: BarracaFormComponent },
  { path: 'barracas/editar/:id', component: BarracaFormComponent },
  { path: '', redirectTo: '/barracas', pathMatch: 'full' },
  { path: '**', redirectTo: '/barracas' }
];
