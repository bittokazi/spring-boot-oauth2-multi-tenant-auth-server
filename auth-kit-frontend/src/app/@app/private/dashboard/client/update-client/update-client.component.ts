import { Component, OnInit } from '@angular/core';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-update-client',
  templateUrl: './update-client.component.html',
  styleUrls: ['./update-client.component.css'],
})
export class UpdateClientComponent implements OnInit {
  public form: any;
  public customErrors: any;
  public loading: Boolean = true;

  constructor(
    private clientService: ClientService,
    private router: Router,
    private sas: SweetAlartService,
    private route: ActivatedRoute
  ) {
    this.form = clientService.generateForm(null);
    this.customErrors = clientService.generateError();
  }

  ngOnInit(): void {
    this.clientService
      .get(this.route.snapshot.params.id)
      .then((res) => {
        this.form = this.clientService.generateForm(res);
      })
      .catch((e) => {
        this.sas.successDialog('Error', 'Error');
      })
      .finally(() => {
        this.loading = false;
      });
  }

  onSubmit(form: FormGroup) {
    this.loading = true;
    this.clientService
      .update(this.form.value)
      .then((res) => {
        if (res.newSecret) {
          this.sas.showConfirmation(
            'Success',
            ``,
            'Dismiss',
            () => {},
            false,
            `<p style="text-align: justify">New Secret Generated<br />
            <span style="font-weight: bold;">ID:</span>&nbsp;${res.clientId}<br />
            <span style="font-weight: bold;">Secret:</span>&nbsp;${res.newSecret}</p>`
          );
        } else {
          this.sas.successDialog('Success', 'Updated');
        }

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
