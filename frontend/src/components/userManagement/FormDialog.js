import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';

export default function FormDialog(props) {
  const [open, setOpen] = React.useState(false);

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      <Button sx={{marginLeft: '10px'}}
              color='primaryButton'
              variant="contained"
              id={props.buttonText}
              onClick={handleOpen}>
        {props.buttonText}
      </Button>
      <Dialog open={open} onClose={handleClose}>
        <DialogContent>
          {props.dialogContent}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
