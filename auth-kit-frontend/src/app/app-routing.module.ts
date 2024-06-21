import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { ErrorNotFoundComponent } from './@app/public/error-not-found/error-not-found.component';
import { PublicModule } from './@app/public/public.module';

const routes: Routes = [
  {
    path: '',
    loadChildren: () =>
      import('./@app/public/public.module').then((m) => m.PublicModule),
  },
  {
    path: '',
    loadChildren: () =>
      import('./@app/private/private.module').then((m) => m.PrivateModule),
  },
  {
    path: '**',
    component: ErrorNotFoundComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes), HttpClientModule, PublicModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
