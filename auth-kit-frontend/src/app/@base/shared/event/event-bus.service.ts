import { Injectable } from '@angular/core';
import { EventInterface } from './EventInterface';

@Injectable({
  providedIn: 'root',
})
export class EventBusService {
  public events: any = {};

  constructor() {}

  emit<T>(key): T {
    if (this.events[key]) {
      return this.events[key];
    }
    return null;
  }

  register<T>(eventInterface: EventInterface<T>) {
    this.events[eventInterface.key] = eventInterface.interface;
  }
}
