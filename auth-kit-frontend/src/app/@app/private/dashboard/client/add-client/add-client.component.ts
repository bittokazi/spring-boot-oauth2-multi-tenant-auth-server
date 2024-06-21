import { Component, OnInit } from '@angular/core';
import { ClientService } from '../client.service';
import { Router } from '@angular/router';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-client',
  templateUrl: './add-client.component.html',
  styleUrls: ['./add-client.component.css'],
})
export class AddClientComponent implements OnInit {
  public form: any;
  public customErrors: any;
  public loading: Boolean = true;

  constructor(
    private clientService: ClientService,
    private router: Router,
    private sas: SweetAlartService
  ) {
    this.form = clientService.generateForm(null);
    this.customErrors = clientService.generateError();
  }

  ngOnInit(): void {
    this.loading = false;
  }

  onSubmit(form: FormGroup) {
    this.loading = true;
    this.clientService
      .add(this.form.value)
      .then((res) => {
        this.sas.showConfirmation(
          'Success',
          ``,
          'Dismiss',
          () => {},
          false,
          `<p style="text-align: justify">New Client Id and Secret Generated<br />
          <span style="font-weight: bold;">ID:</span>&nbsp;${res.clientId}<br />
          <span style="font-weight: bold;">Secret:</span>&nbsp;${res.newSecret}</p>`
        );
        this.router.navigate(['/dashboard/clients']);
      })
      .catch((e) => {
        this.sas.successDialog('Error', 'Error');
      })
      .finally(() => {
        this.loading = false;
      });
  }
}
