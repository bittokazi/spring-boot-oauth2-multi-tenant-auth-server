import { Directive, Input } from "@angular/core";
import { NgControl } from "@angular/forms";

@Directive({
  selector: "[appDisableControl]",
})
export class DisableControlDirective {
  @Input("appDisableControl") condition: boolean;

  constructor(private ngControl: NgControl) {}

  ngOnInit() {
    const action = this.condition ? "disable" : "enable";
    this.ngControl.control[action]();
  }
}
