import { Component, OnInit } from '@angular/core';
import { ClientService } from '../client.service';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';

@Component({
  selector: 'app-list-client',
  templateUrl: './list-client.component.html',
  styleUrls: ['./list-client.component.css'],
})
export class ListClientComponent implements OnInit {
  public clients: any[] = [];

  constructor(
    public clientService: ClientService,
    private sas: SweetAlartService
  ) {}

  ngOnInit(): void {
    this.getList();
  }

  deleteClient(id: string) {
    this.clientService
      .delete(id)
      .then((res) => {
        this.clients = res;
        this.sas.successDialog('Success', 'Deleted oauth client');
        this.getList();
      })
      .catch((e) => {
        if (e.status == 403) {
          this.sas.errorDialog(
            'Access Denied',
            'Deleting default client is not possible.'
          );
        } else {
          this.sas.errorDialog('Error', 'Error Deleting');
        }
      })
      .finally(() => {
        this.onLoad();
      });
  }

  getList() {
    this.clientService
      .getAll()
      .then((res) => {
        this.clients = res;
      })
      .catch((e) => {
        this.sas.errorDialog('Error', 'Error Loading');
      })
      .finally(() => {
        this.onLoad();
      });
  }

  @FeatherIconSetterInjector()
  onLoad() {}
}
